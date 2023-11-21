/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.racetracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RaceTrackerApp() {
    val scope = rememberCoroutineScope()
    val timer = remember { TimerManager(scope = scope) }
    val timerState by timer.timerState.collectAsState()

    // Add this utility function within RaceTrackerApp
    fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (timerState.visible) "Timer: ${formatTime(timerState.timeMillis)}" else "Timer is hidden",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (timerState.visible) Color.Black else Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            timer.startTimer(System.currentTimeMillis(), 10_000) {
                System.currentTimeMillis()
            }
        }) {
            Text("Waiting")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { timer.confirm() }) {
            Text("Confirm")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { timer.reject() }) {
            Text("Reject")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { timer.cancelReject() }) {
            Text("Cancel Reject")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { timer.reloadScreen(System.currentTimeMillis(), 10_000) }) {
            Text("Reload Screen")
        }
    }
}


@Preview
@Composable
fun RaceTrackerAppPreview() {
    MaterialTheme {
        RaceTrackerApp()
    }
}
