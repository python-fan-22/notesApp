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

class MetronomeState(val interval: Long, val metronomeTickLimit: Int, val noteManager: NoteManager) { // interval is in MS
    private val _counter = mutableStateOf(0)
    val counter: Int get() = _counter.value

    private val _metronomeTick = mutableStateOf(0)
    val metronomeTick: Int get() = _metronomeTick.value

    private val _currentNote = mutableStateOf<NoteManager.Note>(NoteManager.Note("", "", ""))
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

                        val newNote = noteManager.selectNote()
                        val newCounter = _counter.value + 1
                        val newTick = if (_metronomeTick.value >= metronomeTickLimit - 1) 0 else _metronomeTick.value + 1

                        _currentNote.value = newNote!!
                        _counter.value = newCounter
                        _metronomeTick.value = newTick
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
}