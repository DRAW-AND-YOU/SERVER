import logging

from src.config import DISCORD_BOT_TOKEN, validate
from src.bot import create_bot

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
)


def main() -> None:
    validate()
    bot = create_bot()
    bot.run(DISCORD_BOT_TOKEN)


if __name__ == "__main__":
    main()
