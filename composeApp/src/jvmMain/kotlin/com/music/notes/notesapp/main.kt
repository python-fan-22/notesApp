package com.music.notes.notesapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.music.notes.notesapp.getNotes

fun main() = application {
    //val notes = getNotes("/Users/reubenocchipinti/IdeaProjects/notesApp/appData/notes.json", 17)

    Window(
        onCloseRequest = ::exitApplication,
        title = "notesapp",
    ) {
        App()
    }
}