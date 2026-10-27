package com.xauusd.intelligence.analysis

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScalpChecklistTest {
    @Test
    fun completeAlignedChecklistRequiresAllConditions() {
        val result = ScalpChecklist.evaluate(
            ScalpChecklistInput(
                timeframe = "M1",
                structure = "Bullish structure",
                momentum = "Bullish",
                keyLevel = "No clear reaction",
                breakConfirmed = true,
                unmitigatedFvgVisible = true,
                m5ConfirmsDirection = true
            )
        )

        assertEquals("CHECKLIST COMPLETE — REVIEW MANUALLY", result.status)
        assertEquals("Bullish", result.direction)
    }

    @Test
    fun missingM5ConfirmationMeansWait() {
        val result = ScalpChecklist.evaluate(
            ScalpChecklistInput(
                timeframe = "M1",
                structure = "Bearish structure",
                momentum = "Bearish",
                keyLevel = "No clear reaction",
                breakConfirmed = true,
                unmitigatedFvgVisible = true,
                m5ConfirmsDirection = false
            )
        )

        assertEquals("WAIT — SETUP NOT CONFIRMED", result.status)
        assertTrue(result.reasons.any { it.contains("M5 directional confirmation") })
    }

    @Test
    fun conflictingResistanceAndBullishBiasMeansWait() {
        val result = ScalpChecklist.evaluate(
            ScalpChecklistInput(
                timeframe = "M5",
                structure = "Bullish structure",
                momentum = "Bullish",
                keyLevel = "Rejected resistance",
                breakConfirmed = true,
                unmitigatedFvgVisible = true,
                m5ConfirmsDirection = true
            )
        )

        assertEquals("WAIT — SETUP NOT CONFIRMED", result.status)
        assertTrue(result.reasons.any { it.contains("conflicts") })
    }
}
