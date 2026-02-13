import asyncio
import json
import logging
from contextlib import AsyncExitStack
from urllib import error, request

from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client

from src.config import GEMINI_API_KEY, GITHUB_TOKEN, NOTION_API_TOKEN
from src.agent.prompts import SYSTEM_PROMPT, build_user_message

logger = logging.getLogger(__name__)

MODEL = "gemini-2.5-pro"
MAX_TURNS = 30
GEMINI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={api_key}"


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
    """MCP 세션에서 도구 목록을 가져와 Gemini 함수 선언 형식으로 변환."""
    result = await session.list_tools()
    tools = []
    for tool in result.tools:
        function_name = f"{server_name}__{tool.name}"
        schema = tool.inputSchema or {"type": "object", "properties": {}}

        tools.append(
            {
                "name": function_name,
                "description": tool.description or "",
                "parameters": schema,
            }
        )
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

    text_parts = []
    for content in result.content:
        if hasattr(content, "text"):
            text_parts.append(content.text)
        else:
            text_parts.append(str(content))
    return "\n".join(text_parts)


def _gemini_request(payload: dict) -> dict:
    url = GEMINI_ENDPOINT.format(model=MODEL, api_key=GEMINI_API_KEY)
    req = request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )

    try:
        with request.urlopen(req, timeout=120) as resp:
            body = resp.read().decode("utf-8")
            return json.loads(body)
    except error.HTTPError as e:
        body = e.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"Gemini API error ({e.code}): {body}") from e
    except error.URLError as e:
        raise RuntimeError(f"Gemini API connection error: {e.reason}") from e


async def _generate_with_gemini(
    contents: list[dict],
    function_declarations: list[dict],
) -> dict:
    payload = {
        "system_instruction": {
            "parts": [{"text": SYSTEM_PROMPT}],
        },
        "contents": contents,
        "tools": [{"function_declarations": function_declarations}],
        "generation_config": {
            "temperature": 0,
            "max_output_tokens": 8192,
        },
    }
    return await asyncio.to_thread(_gemini_request, payload)


def _extract_parts(response: dict) -> list[dict]:
    candidates = response.get("candidates") or []
    if not candidates:
        raise RuntimeError(f"Gemini returned no candidates: {response}")

    content = candidates[0].get("content") or {}
    return content.get("parts") or []


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
    stack = AsyncExitStack()

    try:
        github_read, github_write = await stack.enter_async_context(
            stdio_client(_make_github_params())
        )
        github_session: ClientSession = await stack.enter_async_context(
            ClientSession(github_read, github_write)
        )
        await github_session.initialize()

        notion_read, notion_write = await stack.enter_async_context(
            stdio_client(_make_notion_params())
        )
        notion_session: ClientSession = await stack.enter_async_context(
            ClientSession(notion_read, notion_write)
        )
        await notion_session.initialize()

        sessions = {"github": github_session, "notion": notion_session}

        github_tools = await _collect_tools(github_session, "github")
        notion_tools = await _collect_tools(notion_session, "notion")
        all_tools = github_tools + notion_tools

        logger.info(
            "Loaded %d tools (github: %d, notion: %d)",
            len(all_tools), len(github_tools), len(notion_tools),
        )

        contents: list[dict] = [
            {
                "role": "user",
                "parts": [{"text": build_user_message(domain, endpoint)}],
            }
        ]

        for turn in range(MAX_TURNS):
            logger.info("Agent turn %d/%d", turn + 1, MAX_TURNS)

            response = await _generate_with_gemini(contents, all_tools)
            parts = _extract_parts(response)

            contents.append({"role": "model", "parts": parts})

            function_calls = [
                p["functionCall"]
                for p in parts
                if "functionCall" in p
            ]

            if not function_calls:
                final_text = "\n".join(
                    p.get("text", "")
                    for p in parts
                    if p.get("text")
                ).strip()
                return final_text or "명세서 업데이트가 완료되었습니다."

            function_response_parts: list[dict] = []
            for call in function_calls:
                tool_name = call.get("name", "")
                tool_args = call.get("args") or {}
                logger.info("Calling tool: %s", tool_name)

                try:
                    result_text = await _call_tool(sessions, tool_name, tool_args)
                except Exception as e:
                    logger.exception("Tool call failed: %s", e)
                    result_text = json.dumps({"error": str(e)})

                function_response_parts.append(
                    {
                        "functionResponse": {
                            "name": tool_name,
                            "response": {
                                "content": result_text,
                            },
                        }
                    }
                )

            contents.append({"role": "user", "parts": function_response_parts})

        return "최대 턴 수에 도달했습니다. 일부 작업이 완료되지 않았을 수 있습니다."

    finally:
        await stack.aclose()
