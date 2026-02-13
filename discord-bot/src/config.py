import os
import sys

from dotenv import load_dotenv

load_dotenv()

DISCORD_BOT_TOKEN = os.getenv("DISCORD_BOT_TOKEN", "").strip()
DISCORD_ALLOWED_CHANNEL_IDS: list[int] = [
    int(ch.strip())
    for ch in os.getenv("DISCORD_ALLOWED_CHANNEL_IDS", "").split(",")
    if ch.strip()
]

GEMINI_API_KEY = os.getenv("GEMINI_API_KEY", "").strip()
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN", "").strip()
NOTION_API_TOKEN = os.getenv("NOTION_API_TOKEN", "").strip()

REQUIRED_VARS = {
    "DISCORD_BOT_TOKEN": DISCORD_BOT_TOKEN,
    "GEMINI_API_KEY": GEMINI_API_KEY,
    "GITHUB_TOKEN": GITHUB_TOKEN,
    "NOTION_API_TOKEN": NOTION_API_TOKEN,
}


def validate() -> None:
    missing = [name for name, val in REQUIRED_VARS.items() if not val]
    if missing:
        print(f"[ERROR] Missing required env vars: {', '.join(missing)}")
        sys.exit(1)
