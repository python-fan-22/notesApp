package com.music.notes.notesapp

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.isActive


class NoteAppCompose(val metronomeManagementInstance: MetronomeState) {
    // Using Monospace font as a substitute for JetBrains Mono
    private val jetBrainsMonoFont = FontFamily.Monospace
    
    @Composable
    @Preview
    fun NotesApp() {
        MaterialTheme {
        val counter = metronomeManagementInstance.counter
        var isActive by remember { mutableStateOf(true) }
        val currentNote = metronomeManagementInstance.currentNote
        val metronomeTick = metronomeManagementInstance.metronomeTick
        val metronomeTickLimit = metronomeManagementInstance.metronomeTickLimit

        LaunchedEffect(isActive) {
            if(isActive) {
                metronomeManagementInstance.start()
            }
            else {
                metronomeManagementInstance.stop()
            }
            awaitCancellation()
        }

        DisposableEffect(Unit) {
            onDispose { metronomeManagementInstance.destroy() }

        }

        Box(modifier = Modifier.fillMaxSize()) {
            // Version indicator in top left
            Text(
                text = "NotesApp v0.1",
                fontFamily = jetBrainsMonoFont,
                fontSize = 12.sp,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Light
            )
            
            // Main content column with centered alignment
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Note text at the top
                Text(
                    text = currentNote.parse(),
                    fontFamily = jetBrainsMonoFont,
                    modifier = Modifier.padding(bottom = 30.dp),
                    fontSize = 72.sp,
                    textAlign = TextAlign.Center
                )
                
                // Metronome dots indicator in the middle
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    for (i in 1..metronomeTickLimit) {
                        val isActive = i <= metronomeTick
                        Canvas(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(16.dp)
                        ) {
                            // Draw circle with purple border
                            drawCircle(
                                color = Color(0xFF6200EE), // Purple color
                                radius = size.minDimension / 2,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                            )
                            
                            // Fill circle if active
                            if (isActive) {
                                drawCircle(
                                    color = Color(0xFF6200EE), // Purple color
                                    radius = size.minDimension / 2 - 1.dp.toPx()
                                )
                            }
                        }
                    }
                }
                
                // Pause/resume button at the bottom
                Button(
                    onClick = { isActive = !isActive },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(
                        text = if (isActive) "Pause" else "Start",
                        fontFamily = jetBrainsMonoFont
                    )
                }
            }
        }
        }
    }
}
