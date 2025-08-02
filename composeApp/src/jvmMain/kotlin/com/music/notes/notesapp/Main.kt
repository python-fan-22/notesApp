package com.music.notes.notesapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    // mac path -> /Users/reubenocchipinti/IdeaProjects/notesApp/appData/notes.json
    // linux path -> /home/reuben/IdeaProjects/notesApp/appData/notes.json

    val noteInstance = NoteManager("/home/reuben/IdeaProjects/notesApp/appData/notes.json", 17)
    val metronomeInstance = MetronomeState(800, 12, noteInstance)

    Window(title = "NotesApp", onCloseRequest = ::exitApplication) {
        NoteAppCompose(metronomeInstance).NotesApp()
    }
}
