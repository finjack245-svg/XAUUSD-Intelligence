# Product specification

## Goal
Build a standalone Android companion that examines a user-authorised MT5 screen capture of an XAUUSD chart and explains visible technical structure.

## Confirmed requirements
- Device: Samsung Galaxy A05, Android 14.
- Market: XAUUSD only.
- Timeframes: M1 and M5 only.
- Input: permission-based screen capture initiated by the user.
- Analysis: visible candles, candle patterns, swing highs/lows, tentative trend structure, and visible support/resistance.
- Output: an explanation with timeframe, evidence, uncertainty, and optional local notification.
- No automatic trading or trade management.
- Prefer on-device processing and do not upload captures without explicit informed consent.

## First-release non-goals
- Other symbols or timeframes.
- Broker login or MT5 API integration.
- Order placement, modification, or closure.
- Economic calendar/news feeds.
- Claims of guaranteed prediction or profitability.
- Reliable reconstruction of complete OHLC history from screenshots.

## Acceptance criteria
1. User must explicitly start each screen-capture session and can stop it at any time.
2. The app shows whether capture is active and handles system revocation.
3. User can select M1 or M5; unsupported/unclear timeframe is reported rather than guessed.
4. Low-quality or ambiguous screenshots produce an insufficient-evidence result.
5. Analysis explains which visible features support its conclusion.
6. Notifications are optional, user-controlled, and rate-limited.
7. No code path sends or changes trades.
8. Deterministic analysis rules have unit tests.
