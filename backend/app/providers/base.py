from typing import Protocol


class LLMProvider(Protocol):
    """history is a list of (role, text) with role 'user' or 'model'. Swap vendors by adding another class."""

    async def reply(self, system: str, history: list[tuple[str, str]], user_text: str) -> str: ...
