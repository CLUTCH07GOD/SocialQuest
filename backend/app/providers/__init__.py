from .. import config
from .base import LLMProvider
from .gemini import GeminiProvider


def get_llm() -> LLMProvider | None:
    """FastAPI dependency. Returns None when no key is configured (the gateway then reports llm_not_configured)."""
    key = config.gemini_api_key()
    return GeminiProvider(key, config.gemini_model()) if key else None
