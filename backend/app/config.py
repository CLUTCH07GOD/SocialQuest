"""All settings come from environment variables. Nothing secret lives in code."""
import os


def jwt_secret() -> str:
    secret = os.environ.get("JWT_SECRET", "")
    if len(secret) < 32:
        raise RuntimeError("JWT_SECRET must be set to a random string of 32+ characters. See .env.example")
    return secret


def database_url() -> str:
    return os.environ.get("DATABASE_URL", "sqlite:///./socialquest_dev.db")


def access_token_minutes() -> int:
    return int(os.environ.get("ACCESS_TOKEN_MINUTES", "1440"))


def allowed_origins() -> list[str]:
    return [o.strip() for o in os.environ.get("ALLOWED_ORIGINS", "").split(",") if o.strip()]


def gemini_api_key() -> str | None:
    return os.environ.get("GEMINI_API_KEY") or None


def gemini_model() -> str:
    return os.environ.get("GEMINI_MODEL", "gemini-2.5-flash")
