# Social Quest API contract (v0, owned by Person 1)

Base URL: `http://10.0.2.2:8000/api/v1` from the Android emulator (your laptop's localhost). Machine-readable version: `docs/openapi.json`; live docs at `/docs` when the server runs.
Auth: `Authorization: Bearer <access_token>`. Errors are `{"detail": "<code>"}`. Every endpoint only ever touches the caller's own rows.
Change a contract? Tell Person 1 first, and open a PR that updates this file.

## Auth and eligibility
| Method | Path | Body | Result |
|---|---|---|---|
| POST | `/auth/register` | `email, password (8+), age_confirmed, terms_version, privacy_version, nickname?` | 201 `{access_token}`. `403 age_not_confirmed` creates nothing. `409 email_taken` |
| POST | `/auth/login` | `email, password` | 200 `{access_token}` or `401 invalid_credentials` |
| POST | `/auth/logout` | - | 204. The app deletes its stored token |
| GET | `/me` | - | `{id, email, age_confirmed_at, created_at}` |
| GET | `/me/export` | - | Everything we hold about the user (no password hash) |
| DELETE | `/me` | - | 204. Cascades to every user-owned table |

Register and login are rate limited (10/min per client address): `429 too_many_requests`. Only the 18+ timestamp and policy versions are stored, never a date of birth.

## Consents, profile
| Method | Path | Notes |
|---|---|---|
| GET / POST | `/consents` | POST `{type: terms\|privacy\|mic\|ai_disclosure, version}` |
| GET / PATCH | `/profile` | Fields: `display_name, situations[], difficulties[], goal, preferred_mode (standard\|immersive), captions, reduce_motion, keep_summary`. PATCH changes only the fields you send |

## Sessions (voice practice)
| Method | Path | Notes |
|---|---|---|
| POST | `/sessions` | `{situation_id, mode}` -> session (`status: active`) |
| GET | `/sessions` | Own sessions, newest first |
| PATCH | `/sessions/{id}` | `{status: active\|paused\|ended}`. Ended is final (`409`). Not yours: `404` |

## Progress and XP
| Method | Path | Notes |
|---|---|---|
| POST | `/progress-events` | `{type, ref?, idempotency_key (8-64 chars)}`. 201 first time, 200 on a retry with the same key (nothing awarded twice). The **server** sets `xp_awarded`. Unknown type: `422` |
| GET | `/progress/summary` | `{total_xp, events, by_type}` |

XP rules (`backend/app/xp_rules.py`): `session_started` 5, `session_completed` 20, `reflection_completed` 15, `ladder_step_tried` 10, `quest_completed` 20, `returned_after_difficult_attempt` 15. XP is never negative (also enforced by a database CHECK). No streaks, no rankings.
P3 and P4: to add an event type, ask P1 (one line in `xp_rules.py`). Always send a fresh `idempotency_key` per real action (for example a UUID), and reuse it only when retrying.

## Voice gateway: `WS /ws/sessions/{id}`
1. Client sends `{"type":"auth","token":"<access_token>"}` as the first message. Bad token or someone else's session: `{"type":"error","code":"unauthorized"}`, then the socket closes.
2. Server replies `{"type":"ready"}`.
3. Client sends `{"type":"user_text","text":"..."}` and gets `{"type":"caption","role":"character","text":"..."}`. Errors: `llm_not_configured`, `provider_failed`.
4. Client sends `{"type":"end"}` and gets `{"type":"ended"}`.

Today turns are text, to prove the loop. Audio frames, speech-to-text and text-to-speech plug in later through P3's adapters. The server remembers the last turns in memory only; nothing is written to the database. The Gemini key lives only on the server.

## Tables that don't exist yet
Fear Map, reflections, ladders, predictions, quests, goals and achievements are not in schema v0. Person 2 and Person 4: send P1 your field lists (see plan section 17) and P1 adds the tables and migration.
