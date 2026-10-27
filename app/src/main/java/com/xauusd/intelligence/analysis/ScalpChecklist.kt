package com.xauusd.intelligence.analysis

/**
 * A transparent checklist for a human-reviewed XAUUSD scalping setup.
 * This evaluates user observations only; it does not read a chart or predict profitability.
 */
data class ScalpChecklistInput(
    val timeframe: String,
    val structure: String,
    val momentum: String,
    val keyLevel: String,
    val breakConfirmed: Boolean,
    val unmitigatedFvgVisible: Boolean,
    val m5ConfirmsDirection: Boolean
)

data class ScalpChecklistResult(
    val status: String,
    val reasons: List<String>,
    val direction: String
)

object ScalpChecklist {
    fun evaluate(input: ScalpChecklistInput): ScalpChecklistResult {
        if (input.timeframe != "M1" && input.timeframe != "M5") {
            return ScalpChecklistResult(
                status = "INSUFFICIENT EVIDENCE",
                reasons = listOf("Choose M1 or M5 only."),
                direction = "Neutral"
            )
        }

        val direction = when {
            input.structure == "Bullish structure" && input.momentum == "Bullish" -> "Bullish"
            input.structure == "Bearish structure" && input.momentum == "Bearish" -> "Bearish"
            else -> "Neutral"
        }

        val reasons = mutableListOf<String>()
        if (direction == "Neutral") reasons += "Structure and momentum do not agree."
        if (input.keyLevel == "Rejected resistance" && direction == "Bullish") {
            reasons += "Resistance rejection conflicts with a bullish bias."
        }
        if (input.keyLevel == "Rejected support" && direction == "Bearish") {
            reasons += "Support rejection conflicts with a bearish bias."
        }
        if (input.keyLevel == "Not reviewed") reasons += "Nearby key-level reaction has not been reviewed."
        if (!input.breakConfirmed) reasons += "BOS/CHOCH has not been confirmed in the visible chart."
        if (!input.unmitigatedFvgVisible) reasons += "No clearly visible unmitigated FVG selected."
        if (!input.m5ConfirmsDirection) reasons += "M5 directional confirmation has not been selected."

        val levelConflict =
            (input.keyLevel == "Rejected resistance" && direction == "Bullish") ||
            (input.keyLevel == "Rejected support" && direction == "Bearish")
        val ready = direction != "Neutral" &&
            input.breakConfirmed &&
            input.unmitigatedFvgVisible &&
            input.m5ConfirmsDirection &&
            !levelConflict &&
            input.keyLevel != "No clear reaction" &&
            input.keyLevel != "Not reviewed"

        if (ready) {
            reasons += "Checklist conditions are selected; manually verify the chart and risk before any trade."
            return ScalpChecklistResult("CHECKLIST COMPLETE — REVIEW MANUALLY", reasons, direction)
        }
        if (reasons.isEmpty()) reasons += "More visible evidence is needed before assessing this setup."
        return ScalpChecklistResult("WAIT — SETUP NOT CONFIRMED", reasons, direction)
    }
}
