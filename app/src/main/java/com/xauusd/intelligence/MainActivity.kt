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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("XAUUSD Intelligence", style = MaterialTheme.typography.headlineMedium)
        Text("Gold chart analysis • M1 / M5")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { timeframe = "M1" }) { Text("M1") }
            Button(onClick = { timeframe = "M5" }) { Text("M5") }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Selected timeframe: $timeframe")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Capture is not active.")
                Text("The chart-analysis engine is not implemented yet.")
            }
        }

        Button(
            onClick = { /* Screen capture will be added in the next milestone. */ },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start MT5 screen capture (coming next)")
        }

        Text(
            "Analysis only. No trades will be opened, modified, or closed.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
