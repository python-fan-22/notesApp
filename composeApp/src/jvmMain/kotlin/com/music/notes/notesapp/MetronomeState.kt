package com.music.notes.notesapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import jdk.internal.net.http.common.Log.logError
import jdk.jfr.internal.consumer.EventLog.stop
import jdk.jfr.internal.management.ManagementSupport.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.coroutines.cancellation.CancellationException
import androidx.compose.runtime.State
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineEvent
import kotlin.time.Duration.Companion.milliseconds

class MetronomeState(val interval: Long, val metronomeTickLimit: Int, val noteManager: NoteManager, val audioFile: File) { // interval is in MS
    private val _counter = mutableStateOf(0)
    val counter: Int get() = _counter.value

    private val _metronomeTick = mutableStateOf(0)
    val metronomeTick: Int get() = _metronomeTick.value

    private val _currentNote = mutableStateOf<NoteManager.Note>(noteManager.selectNote())
    val currentNote: NoteManager.Note get() = _currentNote.value

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var timerJob: Job? = null

    fun start() {
        stop()
        timerJob = scope.launch {
            while (isActive) {
                try {
                    withContext(Dispatchers.Default) {
                        //val selectedNote: NoteManager.Note = if (firstRun) noteManager.selectNote()!! else noteManager.selectNote()!!

                        // we're going to implement not playing enharmonic equivalents later.


                        val newCounter = _counter.value + 1
                        val newNote = if (_metronomeTick.value >= metronomeTickLimit ) noteManager.selectNote() else _currentNote.value
                        val newTick = if (_metronomeTick.value >= metronomeTickLimit ) 1 else _metronomeTick.value + 1

                        //if (_metronomeTick.value == metronomeTickLimit) noteManager.selectNote() else _currentNote.value
                        _currentNote.value = newNote
                        _counter.value = newCounter
                        _metronomeTick.value = newTick

                        println(newCounter)
                        metronomePlayAudio()
                        delay(interval)
                    }

                }
                catch (e: CancellationException) {
                    throw e
                }
                catch (e: Exception) {
                    throw e
                }
            }
        }
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
    }

    fun destroy() {
        scope.cancel()
    }

    fun metronomePlayAudio() {
        val tickNoise = AudioSystem.getClip()
        val audioInputStream = AudioSystem.getAudioInputStream(audioFile)

        tickNoise.open(audioInputStream)
        tickNoise.start()

        // Add a listener to close resources when done playing
        tickNoise.addLineListener { event ->
            if (event.type == LineEvent.Type.STOP) {
                tickNoise.close()
                audioInputStream.close()
            }
        }
    }
}