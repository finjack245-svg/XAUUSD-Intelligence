# Agent instructions

Read `README.md` and the relevant files under `docs/` before making changes.

## Product boundaries
- Support XAUUSD only.
- Support M1 and M5 only.
- Use user-authorised Android MediaProjection screen capture only.
- Provide analysis and optional alerts only. Never add trade execution, order modification, or trade-closing code.
- Do not add hidden capture, remote upload, credentials, or analytics that transmit screenshots.
- If chart evidence is unreadable or ambiguous, return an explicit low-confidence/insufficient-evidence result.

## Engineering
- Keep image recognition, market-structure analysis, UI, capture lifecycle, and alerts in separate packages.
- Add unit tests for deterministic analysis rules.
- Avoid claiming exact OHLC prices from pixels unless a validated calibration method supports them.
- Update documentation when scope or permissions change.
- Never commit signing keys, API tokens, personal screenshots, or generated secrets.
