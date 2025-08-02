package com.music.notes.notesapp

import androidx.compose.runtime.State
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.forEach

class NoteManager(val filepath: String, val numNotes: Int) {
    @Serializable
    data class Notes (
        val notes: Map<String, Int>
    )

    class Note(val currentKey: String, val enharmonicEquivalent: String?, val filepath: String) {


        override fun toString(): String {
            return currentKey
        } // this function is kinda redundant but we'll keep it for now. (js use parse on line 29 bro)

        fun playNote(soundFileDirPath: String): Int {
            // soundFileDirPath = "/Users/reubenocchipinti/IdeaProjects/notesApp/appData/noteAudios" btw

            val noteSound = File("soundFileDirPath/$currentKey.mp3")
            TODO("Need to find a way to actually play noises, do this later! NOT priority.")
        }

        fun parse(): String {
            val splitNoteString = currentKey.split("-")
            val coreNote = splitNoteString[0]

            return when (splitNoteString.size) {
                2 if splitNoteString[1] == "flat" -> {
                    "$coreNote♭"
                }
                2 if splitNoteString[1] == "sharp" -> {
                    "$coreNote♯"
                }
                else -> {
                    coreNote
                }
            }
        }
    }

    val noteClassMap: Map<Note, Int>
        get() {
            val notesFile = File(filepath)

            try {
                val rawTextNotes = notesFile.readText()

                val serializedNotes = Json.decodeFromString<Notes>(rawTextNotes)
                val cleanedNotes = serializedNotes.notes.toMap() // normal map that looks like this: "A=-3"
                /*
               UPDATED: the notes themselves are now the keys and the values are their distance away from C (in semitones) this is much
               smarter (credit to me) and makes it so intuitive to recognise enharmonic equivalents
                 */

                val noteClassMap = buildMap<Note, Int>(numNotes) {
                    cleanedNotes.forEach {
                        val currentNote = it.key
                        val currentSemitoneGap = it.value // how many semitones from C it is
                        val potentialEnharmonicEquivalent =
                            cleanedNotes.filter { (key, value) -> value == currentSemitoneGap && key != currentNote }.keys.toList()

                        val enharmonicEquivalent =
                            if (potentialEnharmonicEquivalent.size == 1) potentialEnharmonicEquivalent[0] else null
                        // there has to be a better way to compute the enharmonic equivalent, we'll do that later.

                        val noteInstance = Note(currentNote, enharmonicEquivalent, "/home/reuben/IdeaProjects/notesApp/appData/notes.json")
                        put(noteInstance, it.value)
                    }
                }

                return noteClassMap.toMap()
            }

            catch(e: SerializationException) {
                return emptyMap()
            }
        }

    val loadedNoteMap = noteClassMap
    val pureNoteList = loadedNoteMap.keys.toList()

    fun selectNote(omittedNote: Note = Note("placeholder", "placeholder", "placeholder")
    ): Note {
        val sampleNoteList = pureNoteList - omittedNote
        val selectedNote = sampleNoteList.random()

        return selectedNote
    }
}
