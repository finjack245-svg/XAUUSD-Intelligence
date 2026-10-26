package com.xauusd.intelligence

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
private fun DashboardScreen() {
    var timeframe by rememberSaveable { mutableStateOf("M1") }
    var structure by rememberSaveable { mutableStateOf("Not reviewed") }
    var momentum by rememberSaveable { mutableStateOf("Not reviewed") }
    var keyLevel by rememberSaveable { mutableStateOf("Not reviewed") }
    var reviewed by rememberSaveable { mutableStateOf(false) }

    val summary = when {
        !reviewed -> "No chart review yet"
        structure == "Mixed / ranging" || momentum == "Unclear" ||
            structure == "Not reviewed" || momentum == "Not reviewed" ||
            keyLevel == "Not reviewed" -> "NEUTRAL — insufficient confirmation"
        structure == "Bullish structure" && momentum == "Bullish" &&
            keyLevel == "Rejected resistance" -> "CONFLICT — bullish structure, but resistance rejection needs caution"
        structure == "Bullish structure" && momentum == "Bullish" ->
            "BULLISH BIAS — wait for a clear, confirmed setup"
        structure == "Bearish structure" && momentum == "Bearish" &&
            keyLevel == "Rejected support" -> "CONFLICT — bearish structure, but support rejection needs caution"
        structure == "Bearish structure" && momentum == "Bearish" ->
            "BEARISH BIAS — wait for a clear, confirmed setup"
        else -> "MIXED EVIDENCE — no directional bias"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("XAUUSD", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text("Intelligence", style = MaterialTheme.typography.headlineLarge)
        Text("Your Gold chart companion • Analysis only")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("MARKET", style = MaterialTheme.typography.labelMedium)
                Text("Gold / XAUUSD", style = MaterialTheme.typography.titleLarge)
                Text("Choose the timeframe you are reviewing.")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (timeframe == "M1") Button(onClick = { timeframe = "M1" }) { Text("M1 • Selected") }
                    else OutlinedButton(onClick = { timeframe = "M1" }) { Text("M1") }
                    if (timeframe == "M5") Button(onClick = { timeframe = "M5" }) { Text("M5 • Selected") }
                    else OutlinedButton(onClick = { timeframe = "M5" }) { Text("M5") }
                }
            }
        }

        Text("QUICK CHART REVIEW", style = MaterialTheme.typography.titleMedium)
        Text("Select what you can clearly see on your MT5 chart. This guided review is not automatic image recognition.")

        ReviewChoiceCard(
            title = "1. Market structure",
            options = listOf("Bullish structure", "Bearish structure", "Mixed / ranging"),
            selected = structure,
            onSelect = { structure = it; reviewed = true }
        )
        ReviewChoiceCard(
            title = "2. Recent momentum",
            options = listOf("Bullish", "Bearish", "Unclear"),
            selected = momentum,
            onSelect = { momentum = it; reviewed = true }
        )
        ReviewChoiceCard(
            title = "3. Nearby key level",
            options = listOf("Rejected support", "Rejected resistance", "No clear reaction"),
            selected = keyLevel,
            onSelect = { keyLevel = it; reviewed = true }
        )

        Button(
            onClick = { reviewed = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Review my observations") }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("ASSISTANT SUMMARY", style = MaterialTheme.typography.labelMedium)
                Text(summary, style = MaterialTheme.typography.titleMedium)
                Text("Timeframe: $timeframe")
                Text("Structure: $structure")
                Text("Momentum: $momentum")
                Text("Key level: $keyLevel")
                Text("This is a rule-based summary of your selections, not a live chart signal or a probability of profit.")
            }
        }

        OutlinedButton(
            onClick = {
                structure = "Not reviewed"
                momentum = "Not reviewed"
                keyLevel = "Not reviewed"
                reviewed = false
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Reset review") }

        Spacer(modifier = Modifier.height(4.dp))
        Text("NEXT DEVELOPMENT STEP", style = MaterialTheme.typography.labelLarge)
        Text("Connect user-approved MT5 screen capture, then add on-device chart-quality checks and explainable candle/structure detection.")
        Text(
            "No broker login. No screenshot uploads. No automatic trade placement, modification, or closure.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ReviewChoiceCard(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            options.forEach { option ->
                if (selected == option) {
                    Button(onClick = { onSelect(option) }, modifier = Modifier.fillMaxWidth()) {
                        Text(option)
                    }
                } else {
                    OutlinedButton(onClick = { onSelect(option) }, modifier = Modifier.fillMaxWidth()) {
                        Text(option)
                    }
                }
            }
        }
    }
}
