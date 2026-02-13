import logging
import re
from pathlib import Path

import discord
from discord import app_commands

from src.config import CODE_REVIEW_REPO_PATH, DISCORD_ALLOWED_CHANNEL_IDS
from src.agent.prompts import VALID_DOMAINS
from src.agent.review_agent import run_codex_review
from src.agent.spec_agent import run_spec_agent

logger = logging.getLogger(__name__)

# 자연어에서 도메인을 추출하기 위한 패턴
DOMAIN_PATTERN = re.compile(
    r"(?:(" + "|".join(re.escape(d) for d in VALID_DOMAINS) + r"|all))"
    r".*(?:명세서|spec|api|업데이트|update|동기화|sync)",
    re.IGNORECASE,
)
REVERSE_PATTERN = re.compile(
    r"(?:명세서|spec|api|업데이트|update|동기화|sync)"
    r".*(?:(" + "|".join(re.escape(d) for d in VALID_DOMAINS) + r"|all))",
    re.IGNORECASE,
)

ENDPOINT_PATTERN = re.compile(r"(/api/\S+)")

MAX_DISCORD_LENGTH = 2000
MAX_ERROR_PREVIEW = 1200


def _is_allowed_channel(channel_id: int) -> bool:
    if not DISCORD_ALLOWED_CHANNEL_IDS:
        return True
    return channel_id in DISCORD_ALLOWED_CHANNEL_IDS


def _extract_domain(text: str) -> str | None:
    m = DOMAIN_PATTERN.search(text) or REVERSE_PATTERN.search(text)
    if m:
        return m.group(1).lower()
    return None


def _extract_endpoint(text: str) -> str | None:
    m = ENDPOINT_PATTERN.search(text)
    return m.group(1) if m else None


def _split_message(text: str) -> list[str]:
    """Discord 메시지 길이 제한(2000자)에 맞춰 분할."""
    if len(text) <= MAX_DISCORD_LENGTH:
        return [text]
    chunks = []
    while text:
        if len(text) <= MAX_DISCORD_LENGTH:
            chunks.append(text)
            break
        split_at = text.rfind("\n", 0, MAX_DISCORD_LENGTH)
        if split_at == -1:
            split_at = MAX_DISCORD_LENGTH
        chunks.append(text[:split_at])
        text = text[split_at:].lstrip("\n")
    return chunks


def _build_error_text(error: Exception) -> str:
    text = str(error).strip() or error.__class__.__name__
    if len(text) > MAX_ERROR_PREVIEW:
        text = text[:MAX_ERROR_PREVIEW] + "... (truncated)"
    return f"명세서 업데이트 중 오류가 발생했습니다:\n{text}"


def create_bot() -> discord.Client:
    intents = discord.Intents.default()
    intents.message_content = True

    bot = discord.Client(intents=intents)
    tree = app_commands.CommandTree(bot)

    # --- 슬래시 커맨드: /sync-spec ---
    @tree.command(name="sync-spec", description="API 명세서를 최신 코드 기반으로 Notion에 업데이트합니다")
    @app_commands.describe(
        domain="업데이트할 도메인 (예: user, article, diary, all)",
        endpoint="특정 엔드포인트 경로 (예: /api/user/profile). 생략하면 도메인 전체 업데이트",
    )
    @app_commands.choices(
        domain=[app_commands.Choice(name=d, value=d) for d in VALID_DOMAINS + ["all"]]
    )
    async def sync_spec(
        interaction: discord.Interaction,
        domain: str,
        endpoint: str | None = None,
    ) -> None:
        if not _is_allowed_channel(interaction.channel_id):
            await interaction.response.send_message(
                "이 채널에서는 사용할 수 없습니다.", ephemeral=True
            )
            return

        await interaction.response.defer(thinking=True)
        target = f"{domain} {endpoint}" if endpoint else domain
        logger.info("sync-spec command: target=%s by %s", target, interaction.user)

        try:
            result = await run_spec_agent(domain, endpoint)
            for chunk in _split_message(f"**[{target}] 명세서 업데이트 완료**\n\n{result}"):
                await interaction.followup.send(chunk)
        except Exception as e:
            logger.exception("Agent failed for target=%s", target)
            for chunk in _split_message(_build_error_text(e)):
                await interaction.followup.send(chunk)

    # --- 슬래시 커맨드: /codex-review ---
    @tree.command(name="codex-review", description="Codex CLI로 Git 변경사항 코드 리뷰를 수행합니다")
    @app_commands.describe(
        target="working(기본), staged, 또는 비교 브랜치명(예: main, dev)",
    )
    async def codex_review(
        interaction: discord.Interaction,
        target: str | None = None,
    ) -> None:
        if not _is_allowed_channel(interaction.channel_id):
            await interaction.response.send_message(
                "이 채널에서는 사용할 수 없습니다.", ephemeral=True
            )
            return

        await interaction.response.defer(thinking=True)
        normalized_target = (target or "working").strip() or "working"
        logger.info("codex-review command: target=%s by %s", normalized_target, interaction.user)

        try:
            repo_path = Path(CODE_REVIEW_REPO_PATH) if CODE_REVIEW_REPO_PATH else None
            result = await run_codex_review(normalized_target, repo_path=repo_path)
            for chunk in _split_message(f"**[{normalized_target}] 코드 리뷰 완료**\n\n{result}"):
                await interaction.followup.send(chunk)
        except Exception as e:
            logger.exception("Codex review failed for target=%s", normalized_target)
            err = str(e).strip() or e.__class__.__name__
            if len(err) > MAX_ERROR_PREVIEW:
                err = err[:MAX_ERROR_PREVIEW] + "... (truncated)"
            for chunk in _split_message(f"코드 리뷰 중 오류가 발생했습니다:\n{err}"):
                await interaction.followup.send(chunk)

    # --- 자연어 메시지 핸들러 ---
    @bot.event
    async def on_message(message: discord.Message) -> None:
        if message.author == bot.user or message.author.bot:
            return
        if not _is_allowed_channel(message.channel.id):
            return

        domain = _extract_domain(message.content)
        if domain is None:
            return

        endpoint = _extract_endpoint(message.content)
        target = f"{domain} {endpoint}" if endpoint else domain
        logger.info("Natural language trigger: target=%s by %s", target, message.author)

        async with message.channel.typing():
            try:
                result = await run_spec_agent(domain, endpoint)
                for chunk in _split_message(f"**[{target}] 명세서 업데이트 완료**\n\n{result}"):
                    await message.reply(chunk)
            except Exception as e:
                logger.exception("Agent failed for target=%s", target)
                for chunk in _split_message(_build_error_text(e)):
                    await message.reply(chunk)

    # --- Bot ready ---
    @bot.event
    async def on_ready() -> None:
        await tree.sync()
        logger.info("Bot ready as %s (synced slash commands)", bot.user)

    return bot
