# Initial technical-analysis rules

These rules describe the initial behaviour. The manual scalping checklist is implemented; automatic chart recognition is not yet implemented.

## Inputs
- A single user-approved screenshot.
- User-selected timeframe: M1 or M5.
- Chart must be recognisably XAUUSD.
- Visible candle area must pass image-quality checks.

## Observation vs interpretation
Keep observations separate from interpretations. Example:
- Observation: the latest visible candle has a relatively long lower wick.
- Interpretation: this may indicate rejection of lower prices, but one candle alone is not a signal.

## First rules to implement
- Detect whether candle bodies and wicks are sufficiently clear.
- Identify candidate swing highs/lows only when enough neighbouring candles are visible.
- Describe structure as bullish, bearish, range-bound, mixed, or insufficient evidence.
- Mark BOS/CHOCH only when prior swing levels can be identified with adequate confidence.
- Identify simple candle patterns only when their defining geometry is visible.
- Compare M1 and M5 results only when both analyses are recent and usable.

## Confidence and safety
- Confidence refers to image/feature evidence quality, not probability of profit.
- Never manufacture price levels or claim statistical win rates from screenshots.
- Return `insufficient evidence` for cropped, blurred, obscured, or ambiguous charts.
- No analysis result can trigger an order action.
