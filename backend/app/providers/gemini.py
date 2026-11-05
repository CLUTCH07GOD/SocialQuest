import httpx

_URL = "https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent"


class GeminiProvider:
    def __init__(self, api_key: str, model: str):
        self._key, self._model = api_key, model

    async def reply(self, system: str, history: list[tuple[str, str]], user_text: str) -> str:
        contents = [{"role": r, "parts": [{"text": t}]} for r, t in history]
        contents.append({"role": "user", "parts": [{"text": user_text}]})
        body = {"system_instruction": {"parts": [{"text": system}]}, "contents": contents}
        async with httpx.AsyncClient(timeout=20) as client:
            resp = await client.post(_URL.format(model=self._model), headers={"x-goog-api-key": self._key}, json=body)
        resp.raise_for_status()
        return resp.json()["candidates"][0]["content"]["parts"][0]["text"].strip()
