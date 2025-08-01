package com.music.notes.notesapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.music.notes.notesapp.Notes

fun main() = application {
    // mac path -> /Users/reubenocchipinti/IdeaProjects/notesApp/appData/notes.json
    // linux path -> /home/reuben/IdeaProjects/notesApp/appData/notes.json

    val loadedNotes = getNotes("/home/reuben/IdeaProjects/notesApp/appData/notes.json", 17)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Notes App",
    ) {
        App()
    }
}