from datetime import datetime
from typing import Literal

from pydantic import BaseModel, EmailStr, Field

Mode = Literal["standard", "immersive"]
ConsentType = Literal["terms", "privacy", "mic", "ai_disclosure"]
ShortText = Field(max_length=100)


class RegisterIn(BaseModel):
    email: EmailStr
    password: str = Field(min_length=8, max_length=128)
    age_confirmed: bool
    terms_version: str = Field(max_length=20)
    privacy_version: str = Field(max_length=20)
    nickname: str | None = Field(default=None, max_length=40)


class LoginIn(BaseModel):
    email: EmailStr
    password: str = Field(min_length=1, max_length=128)


class TokenOut(BaseModel):
    access_token: str
    token_type: Literal["bearer"] = "bearer"


class MeOut(BaseModel):
    id: str
    email: str
    age_confirmed_at: datetime
    created_at: datetime


class ConsentIn(BaseModel):
    type: ConsentType
    version: str = Field(max_length=20)


class ConsentOut(ConsentIn):
    accepted_at: datetime


class ProfileOut(BaseModel):
    display_name: str | None
    situations: list[str]
    difficulties: list[str]
    goal: str | None
    preferred_mode: Mode
    captions: bool
    reduce_motion: bool
    keep_summary: bool


class ProfilePatch(BaseModel):
    """Only the fields you send are changed."""
    display_name: str | None = Field(default=None, max_length=40)
    situations: list[str] | None = Field(default=None, max_length=20)
    difficulties: list[str] | None = Field(default=None, max_length=20)
    goal: str | None = Field(default=None, max_length=300)
    preferred_mode: Mode | None = None
    captions: bool | None = None
    reduce_motion: bool | None = None
    keep_summary: bool | None = None


class SessionCreate(BaseModel):
    situation_id: str = Field(min_length=1, max_length=100)
    mode: Mode = "standard"


class SessionPatch(BaseModel):
    status: Literal["active", "paused", "ended"]


class SessionOut(BaseModel):
    id: str
    situation_id: str
    mode: Mode
    status: Literal["active", "paused", "ended"]
    started_at: datetime
    ended_at: datetime | None


class ProgressEventIn(BaseModel):
    type: str = Field(max_length=50)
    ref: str | None = Field(default=None, max_length=100)
    idempotency_key: str = Field(min_length=8, max_length=64)


class ProgressEventOut(BaseModel):
    id: int
    type: str
    ref: str | None
    xp_awarded: int
    created_at: datetime


class ProgressSummary(BaseModel):
    total_xp: int
    events: int
    by_type: dict[str, int]
