import json
import logging
from contextlib import AsyncExitStack

import anthropic
from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client

from src.config import ANTHROPIC_API_KEY, GITHUB_TOKEN, NOTION_API_TOKEN
from src.agent.prompts import SYSTEM_PROMPT, build_user_message

logger = logging.getLogger(__name__)

MODEL = "claude-sonnet-4-5-20250929"
MAX_TURNS = 30


def _make_github_params() -> StdioServerParameters:
    return StdioServerParameters(
        command="docker",
        args=[
            "run", "-i", "--rm",
            "-e", "GITHUB_PERSONAL_ACCESS_TOKEN",
            "ghcr.io/github/github-mcp-server",
        ],
        env={
            "GITHUB_PERSONAL_ACCESS_TOKEN": GITHUB_TOKEN,
            "DOCKER_API_VERSION": "1.43",
        },
    )


def _make_notion_params() -> StdioServerParameters:
    return StdioServerParameters(
        command="npx",
        args=["-y", "@notionhq/notion-mcp-server"],
        env={"NOTION_API_TOKEN": NOTION_API_TOKEN},
    )


async def _collect_tools(
    session: ClientSession, server_name: str
) -> list[dict]:
    """MCP 세션에서 도구 목록을 가져와 Claude API 형식으로 변환."""
    result = await session.list_tools()
    tools = []
    for tool in result.tools:
        tool_schema = {
            "name": f"{server_name}__{tool.name}",
            "description": tool.description or "",
            "input_schema": tool.inputSchema,
        }
        tools.append(tool_schema)
    return tools


async def _call_tool(
    sessions: dict[str, ClientSession], tool_name: str, tool_input: dict
) -> str:
    """서버 접두어를 파싱하여 적절한 MCP 세션으로 도구 호출을 라우팅."""
    parts = tool_name.split("__", 1)
    if len(parts) != 2:
        return json.dumps({"error": f"Invalid tool name format: {tool_name}"})

    server_name, actual_tool_name = parts
    session = sessions.get(server_name)
    if session is None:
        return json.dumps({"error": f"Unknown server: {server_name}"})

    result = await session.call_tool(actual_tool_name, tool_input)

    # MCP 결과를 텍스트로 변환
    text_parts = []
    for content in result.content:
        if hasattr(content, "text"):
            text_parts.append(content.text)
        else:
            text_parts.append(str(content))
    return "\n".join(text_parts)


async def run_spec_agent(domain: str, endpoint: str | None = None) -> str:
    """
    지정된 도메인의 API 명세서를 GitHub 코드 기반으로 분석하고
    Notion에 업데이트하는 에이전트를 실행합니다.

    Args:
        domain: 대상 도메인 (예: "user", "article", "all")
        endpoint: 특정 엔드포인트 경로 (예: "/api/user/profile"). None이면 도메인 전체.

    Returns:
        에이전트의 최종 응답 텍스트
    """
    client = anthropic.AsyncAnthropic(api_key=ANTHROPIC_API_KEY)
    stack = AsyncExitStack()

    try:
        # GitHub MCP 서버 연결
        github_read, github_write = await stack.enter_async_context(
            stdio_client(_make_github_params())
        )
        github_session: ClientSession = await stack.enter_async_context(
            ClientSession(github_read, github_write)
        )
        await github_session.initialize()

        # Notion MCP 서버 연결
        notion_read, notion_write = await stack.enter_async_context(
            stdio_client(_make_notion_params())
        )
        notion_session: ClientSession = await stack.enter_async_context(
            ClientSession(notion_read, notion_write)
        )
        await notion_session.initialize()

        sessions = {"github": github_session, "notion": notion_session}

        # 두 서버에서 도구 목록 수집
        github_tools = await _collect_tools(github_session, "github")
        notion_tools = await _collect_tools(notion_session, "notion")
        all_tools = github_tools + notion_tools

        logger.info(
            "Loaded %d tools (github: %d, notion: %d)",
            len(all_tools), len(github_tools), len(notion_tools),
        )

        # 초기 메시지 구성
        messages = [
            {"role": "user", "content": build_user_message(domain, endpoint)},
        ]

        # Agentic loop
        for turn in range(MAX_TURNS):
            logger.info("Agent turn %d/%d", turn + 1, MAX_TURNS)

            response = await client.messages.create(
                model=MODEL,
                max_tokens=8192,
                system=SYSTEM_PROMPT,
                tools=all_tools,
                messages=messages,
            )

            # 응답에서 텍스트와 tool_use 블록 분리
            assistant_content = response.content
            messages.append({"role": "assistant", "content": assistant_content})

            # stop_reason이 end_turn이면 완료
            if response.stop_reason == "end_turn":
                # 최종 텍스트 응답 추출
                final_text = ""
                for block in assistant_content:
                    if hasattr(block, "text"):
                        final_text += block.text
                return final_text or "명세서 업데이트가 완료되었습니다."

            # tool_use 블록이 있으면 실행
            tool_results = []
            for block in assistant_content:
                if block.type == "tool_use":
                    logger.info("Calling tool: %s", block.name)
                    try:
                        result_text = await _call_tool(
                            sessions, block.name, block.input
                        )
                    except Exception as e:
                        logger.error("Tool call failed: %s", e)
                        result_text = json.dumps({"error": str(e)})

                    tool_results.append(
                        {
                            "type": "tool_result",
                            "tool_use_id": block.id,
                            "content": result_text,
                        }
                    )

            if tool_results:
                messages.append({"role": "user", "content": tool_results})
            else:
                # tool_use도 없고 end_turn도 아닌 경우 (예: max_tokens 도달)
                break

        return "최대 턴 수에 도달했습니다. 일부 작업이 완료되지 않았을 수 있습니다."

    finally:
        await stack.aclose()
