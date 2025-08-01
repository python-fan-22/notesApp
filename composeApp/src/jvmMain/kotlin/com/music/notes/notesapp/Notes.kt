package com.music.notes.notesapp

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.forEach

interface TODONote {
    val noteValue: String
    val enharmonicEquivalent: String
//    fun playNote(): Int -> I'm going to leave this part commented, work on this AFTER the core functionality is there
    // we're going to make it so that the enharmonic equivalents are never picked right after each other
}

class Note(currentKey: String, enharmonicEquivalent: String?)

    fun playNote(note: String, soundFileDirPath: String): Int {
        // soundFileDirPath = "/Users/reubenocchipinti/IdeaProjects/notesApp/appData/noteAudios" btw

        val noteSound = File("soundFileDirPath/$note.mp3")
        TODO("Need to find a way to actually play noises, do this later! NOT priority.")
    }

    fun parse(note: String): String {
        val splitNoteString = note.split("-")
        val coreNote = splitNoteString[0]

        if (splitNoteString[1] == "flat") {
            return "$coreNote♭"
        } else if (splitNoteString[1] == "sharp") {
            return "$coreNote#"
        }
        return coreNote
    }


@Serializable
data class Notes (
    val notes: Map<String, Int>
)

fun getNotes(filepath: String, numNotes: Int): Map<Note, Int> {
    val notesFile = File(filepath)

    try {
        val rawTextNotes = notesFile.readText()

        val serializedNotes = Json.decodeFromString<Notes>(rawTextNotes)
        val cleanedNotes = serializedNotes.notes.toMap() // normal map that looks like this: "A=-3"
        /*
       UPDATED: the notes themselves are now the keys and the values are their distance away from C (in semitones) this is much
       smarter (credit to me) and makes it so intuitive to recognise enharmonic equivalents
         */

        val noteClassMap = buildMap<Note, Int> (numNotes) {
            cleanedNotes.forEach {
                val currentNote = it.key
                val currentSemitoneGap = it.value // how many semitones from C it is
                val potentialEnharmonicEquivalent  = cleanedNotes.filter { (key, value) -> value == currentSemitoneGap && key != currentNote }.keys.toList()

                val enharmonicEquivalent = if (potentialEnharmonicEquivalent.size == 1) potentialEnharmonicEquivalent[0] else null
                // there has to be a better way to compute the enharmonic equivalent, we'll do that later.

                val noteInstance = Note(currentNote, enharmonicEquivalent)
                put(noteInstance, it.value)
            }
        }

        return noteClassMap.toMap()
        }

    catch(e: SerializationException) {
        return emptyMap()
    }
}
