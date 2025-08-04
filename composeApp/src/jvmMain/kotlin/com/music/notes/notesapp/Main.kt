package com.music.notes.notesapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.nio.file.Paths

fun main() = application {
    // Get the current working directory
    val currentDir = System.getProperty("user.dir")
    
    // Construct paths relative to the current directory
    val notesJsonPath = Paths.get(currentDir, "appData", "notes.json").toString()
    val tickWavPath = Paths.get(currentDir, "appData", "images", "sound", "tick.wav").toString()
    
    val noteInstance = NoteManager(notesJsonPath, 17)
    val metronomeInstance = MetronomeState(750, 12, noteInstance,
        File(tickWavPath)
    )

    Window(title = "NotesApp", onCloseRequest = ::exitApplication) {
        NoteAppCompose(metronomeInstance).NotesApp()
    }
}
