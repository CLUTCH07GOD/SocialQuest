"""Schema v0 (Person 1 owns migrations). Other modules add their tables by asking P1; never create your own DB."""
import uuid
from datetime import datetime, timezone

from sqlalchemy import JSON, Boolean, CheckConstraint, DateTime, ForeignKey, Integer, String, Text, UniqueConstraint
from sqlalchemy.orm import Mapped, mapped_column, relationship

from .db import Base


def _uuid() -> str:
    return str(uuid.uuid4())


def _now() -> datetime:
    return datetime.now(timezone.utc)


def _cascade():
    return relationship(cascade="all, delete-orphan")


class User(Base):
    __tablename__ = "users"
    id: Mapped[str] = mapped_column(String(36), primary_key=True, default=_uuid)
    email: Mapped[str] = mapped_column(String(320), unique=True, index=True)
    password_hash: Mapped[str] = mapped_column(String(255))
    # Only the 18+ confirmation is stored: a timestamp and the policy versions. Never a date of birth.
    age_confirmed_at: Mapped[datetime] = mapped_column(DateTime(timezone=True))
    terms_version: Mapped[str] = mapped_column(String(20))
    privacy_version: Mapped[str] = mapped_column(String(20))
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=_now)

    profile: Mapped["Profile"] = relationship(cascade="all, delete-orphan", uselist=False)
    consents: Mapped[list["Consent"]] = _cascade()
    sessions: Mapped[list["SessionRecord"]] = _cascade()
    progress_events: Mapped[list["ProgressEvent"]] = _cascade()


class Consent(Base):
    __tablename__ = "consents"
    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    user_id: Mapped[str] = mapped_column(ForeignKey("users.id", ondelete="CASCADE"), index=True)
    type: Mapped[str] = mapped_column(String(20))  # terms | privacy | mic | ai_disclosure
    version: Mapped[str] = mapped_column(String(20))
    accepted_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=_now)
    __table_args__ = (CheckConstraint("type IN ('terms','privacy','mic','ai_disclosure')", name="ck_consent_type"),)


class Profile(Base):
    __tablename__ = "profiles"
    user_id: Mapped[str] = mapped_column(ForeignKey("users.id", ondelete="CASCADE"), primary_key=True)
    display_name: Mapped[str | None] = mapped_column(String(40), nullable=True)
    situations: Mapped[list] = mapped_column(JSON, default=list)      # onboarding: hard situations
    difficulties: Mapped[list] = mapped_column(JSON, default=list)    # onboarding: what makes them hard
    goal: Mapped[str | None] = mapped_column(Text, nullable=True)
    preferred_mode: Mapped[str] = mapped_column(String(20), default="standard")
    captions: Mapped[bool] = mapped_column(Boolean, default=True)
    reduce_motion: Mapped[bool] = mapped_column(Boolean, default=False)
    keep_summary: Mapped[bool] = mapped_column(Boolean, default=False)
    __table_args__ = (CheckConstraint("preferred_mode IN ('standard','immersive')", name="ck_profile_mode"),)


class SessionRecord(Base):
    __tablename__ = "sessions"
    id: Mapped[str] = mapped_column(String(36), primary_key=True, default=_uuid)
    user_id: Mapped[str] = mapped_column(ForeignKey("users.id", ondelete="CASCADE"), index=True)
    situation_id: Mapped[str] = mapped_column(String(100))
    mode: Mapped[str] = mapped_column(String(20), default="standard")
    status: Mapped[str] = mapped_column(String(20), default="active")
    started_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=_now)
    ended_at: Mapped[datetime | None] = mapped_column(DateTime(timezone=True), nullable=True)
    # No audio columns, and no transcript columns: conversation text is never persisted by the gateway.
    __table_args__ = (
        CheckConstraint("status IN ('active','paused','ended')", name="ck_session_status"),
        CheckConstraint("mode IN ('standard','immersive')", name="ck_session_mode"),
    )


class ProgressEvent(Base):
    __tablename__ = "progress_events"
    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    user_id: Mapped[str] = mapped_column(ForeignKey("users.id", ondelete="CASCADE"), index=True)
    type: Mapped[str] = mapped_column(String(50))
    ref: Mapped[str | None] = mapped_column(String(100), nullable=True)
    xp_awarded: Mapped[int] = mapped_column(Integer)
    idempotency_key: Mapped[str] = mapped_column(String(64))
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), default=_now)
    __table_args__ = (
        CheckConstraint("xp_awarded >= 0", name="ck_progress_xp_nonneg"),
        UniqueConstraint("user_id", "idempotency_key", name="uq_progress_user_key"),
    )
