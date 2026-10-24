# Test plan

## Capture lifecycle
- Consent accepted and capture starts.
- Consent declined and capture does not start.
- User stops capture.
- Android revokes capture or the device locks.
- App handles rotation/resizing and releases resources.
- Capture does not silently resume after process death.

## Image quality
- Clear XAUUSD M1 chart.
- Clear XAUUSD M5 chart.
- Unsupported symbol or timeframe.
- Cropped chart, low resolution, blur, overlays, and popups.
- Dark/light chart themes and different zoom levels.

## Analysis rules
- Synthetic candle geometry tests for long wick, doji-like body, and engulfing candidates.
- Swing-high and swing-low test cases.
- Bullish, bearish, range, mixed, and insufficient-evidence structures.
- BOS/CHOCH tests with known prior swing levels.
- Conflicting M1 and M5 direction.
- Repeated screenshots should not spam notifications.

## Release gate
Do not describe the app as reliable until tests on a labelled screenshot set show measured false-positive and false-negative rates. No real-money use should be encouraged during early testing.
