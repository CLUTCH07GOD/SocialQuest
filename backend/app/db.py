from collections.abc import Iterator

from sqlalchemy import create_engine, event
from sqlalchemy.orm import DeclarativeBase, Session, sessionmaker

from . import config


class Base(DeclarativeBase):
    pass


def make_engine(url: str, **kwargs):
    is_sqlite = url.startswith("sqlite")
    engine = create_engine(url, connect_args={"check_same_thread": False} if is_sqlite else {}, **kwargs)
    if is_sqlite:
        @event.listens_for(engine, "connect")
        def _enable_fk(dbapi_conn, _record):  # cascade deletes need this on SQLite
            cur = dbapi_conn.cursor()
            cur.execute("PRAGMA foreign_keys=ON")
            cur.close()
    return engine


_maker: sessionmaker | None = None


def get_db() -> Iterator[Session]:
    global _maker
    if _maker is None:
        _maker = sessionmaker(bind=make_engine(config.database_url()), expire_on_commit=False)
    db = _maker()
    try:
        yield db
    finally:
        db.close()
