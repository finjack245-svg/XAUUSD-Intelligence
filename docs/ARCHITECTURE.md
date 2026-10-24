# Architecture

## Data flow
1. The user opens MT5 and selects an XAUUSD chart on M1 or M5.
2. The user starts capture from this app and approves Android's system consent prompt.
3. Capture manager obtains frames while the session is active.
4. Image-quality and chart-region checks decide whether the frame is usable.
5. Vision components estimate candle locations and visible features; they must preserve uncertainty.
6. Analysis components evaluate candle patterns and swing/market structure.
7. UI displays a timestamped result; the alert component may issue a local notification if enabled.
8. Capture resources are released when the user stops capture or Android revokes the session.

## Package boundaries
- `ui/`: Compose screens and presentation state.
- `capture/`: MediaProjection consent/session lifecycle and frame delivery.
- `vision/`: chart-region, candle, and image-quality processing.
- `analysis/`: deterministic technical rules and confidence/quality labels.
- `alerts/`: local notifications and duplicate suppression.
- `settings/`: user preferences.

## Principles
- Keep processing on-device in the first release.
- Do not persist screenshots by default.
- Never infer exact price values from pixels without a tested calibration process.
- Separate observed facts (e.g. a visible long wick) from interpretations (e.g. possible rejection).
- A screenshot is a snapshot, not a live market-data feed.
