package com.example.racetracker.ui

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerManager(private val scope: CoroutineScope) {
    private var timerJob: Job? = null
    private var startTime: Long = 0
    private var duration: Long = 0

    private val _timerState = MutableStateFlow(TimerState())
    val timerState = _timerState.asStateFlow()

    fun startTimer(startedAtMillis: Long, durationMillis: Long, currentTimeProvider: () -> Long ) {
        startTime = startedAtMillis
        duration = durationMillis
        timerJob = scope.launch {
            while (isActive) {
                val currentTime = currentTimeProvider()
                val timeLeft = startTime + duration - currentTime
                if (timeLeft > 0) {
                    _timerState.value = TimerState(visible = true, timeMillis = timeLeft)
                    delay(1000)
                } else {
                    _timerState.value = TimerState(visible = false, timeMillis = 0)
                    break
                }
            }
        }
    }

    fun confirm() {
        timerJob?.cancel()
        _timerState.value = TimerState(visible = false, timeMillis = 0)
    }

    fun reject() {
        _timerState.value = _timerState.value.copy(visible = false)
    }

    fun cancelReject() {
        _timerState.value = _timerState.value.copy(visible = true)
    }

    fun reloadScreen(startedAtMillis: Long, durationMillis: Long) {
        if (startedAtMillis != null) {
            startTimer(startedAtMillis, durationMillis) {
                System.currentTimeMillis()
            }
        } else {
            _timerState.value = TimerState(visible = false, timeMillis = 0)
        }
    }
    data class TimerState(val visible: Boolean = false, val timeMillis: Long = 0)
}
