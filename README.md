# XAUUSD Intelligence

A standalone Android app project for **XAUUSD (gold) chart analysis** on a phone.

## First release

- Market scope: XAUUSD only.
- Timeframes: M1 and M5 only.
- Input: user-approved Android screen capture of the MT5 chart.
- Analysis: visible candlestick patterns, swing highs/lows, and tentative market structure.
- Output: an explainable analysis summary and optional local alerts.
- Safety: advisory only. No order placement, modification, or closure.
- Privacy: screenshots should be processed on-device where practical and must not be uploaded without explicit consent.

## Important limitations

Screen captures are images, not a reliable source of exact OHLC values or complete historical candles. The app must report uncertainty or reject an unreadable image rather than inventing prices or claiming certainty. The first release does not include economic-news feeds or a historical price database.

## Project status

This repository starts with the product and architecture documents plus a minimal Android application shell. Chart recognition and technical-analysis rules will be implemented and tested incrementally.

## Documentation

- [Product specification](docs/PRODUCT_SPEC.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Screen capture and privacy](docs/SCREEN_CAPTURE.md)
- [Analysis rules](docs/ANALYSIS_RULES.md)
- [Test plan](docs/TEST_PLAN.md)

## Build

Open the repository root in a recent Android Studio release, allow Gradle sync, then run the `app` configuration on an Android device or emulator. Android Studio may generate or update local Gradle wrapper files as needed.

Do not use real-money trading decisions based on unvalidated output. This app is an experimental visual-analysis aid, not financial advice.
