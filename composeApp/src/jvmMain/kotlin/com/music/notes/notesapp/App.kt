package com.music.notes.notesapp

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.isActive


class NoteAppCompose(val metronomeManagementInstance: MetronomeState) {
    @Composable
    @Preview
    fun NotesApp() {
        MaterialTheme {
        val counter = metronomeManagementInstance.counter
        var isActive by remember { mutableStateOf(true) }
        val currentNote = metronomeManagementInstance.currentNote

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

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Today's date is ${currentNote.parse()}",
                modifier = Modifier.padding(20.dp),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Button(onClick = { isActive = !isActive }) {
                Text("Click me!")
            }
        }
        }
    }
}
