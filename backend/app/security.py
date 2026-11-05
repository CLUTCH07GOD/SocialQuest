from datetime import datetime, timedelta, timezone

import jwt
from argon2 import PasswordHasher
from argon2.exceptions import VerificationError

from . import config

_hasher = PasswordHasher()
_DUMMY_HASH = _hasher.hash("not-a-real-password")  # lets login take similar time for unknown emails


def hash_password(password: str) -> str:
    return _hasher.hash(password)


def verify_password(password_hash: str | None, password: str) -> bool:
    try:
        return _hasher.verify(password_hash or _DUMMY_HASH, password) and password_hash is not None
    except VerificationError:
        return False


def create_token(user_id: str) -> str:
    exp = datetime.now(timezone.utc) + timedelta(minutes=config.access_token_minutes())
    return jwt.encode({"sub": user_id, "exp": exp}, config.jwt_secret(), algorithm="HS256")


def decode_token(token: str) -> str | None:
    """Returns the user id, or None for any invalid or expired token."""
    try:
        return jwt.decode(token, config.jwt_secret(), algorithms=["HS256"]).get("sub")
    except jwt.PyJWTError:
        return None
