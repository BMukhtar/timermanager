package com.example.racetracker.ui


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

@ExperimentalCoroutinesApi
class TimerManagerTest {

    private val testScope = TestScope()

    @Test
    fun `startTimer updates timerState correctly`() = testScope.runTest {
        val timerManager = TimerManager(this)
        val currentTime = System.currentTimeMillis()
        timerManager.startTimer(currentTime, 5000) { currentTime + currentTimeElapsed }

        advanceTimeBy(1000)
        assertEquals(4000, timerManager.timerState.value.timeMillis)
        assertTrue(timerManager.timerState.value.visible)

        advanceTimeBy(4000)
        assertEquals(0, timerManager.timerState.value.timeMillis)
        assertFalse(timerManager.timerState.value.visible)
    }

    @Test
    fun `confirm cancels timer and updates timerState correctly`() = testScope.runTest  {
        val timerManager = TimerManager(this)
        timerManager.startTimer(System.currentTimeMillis(), 5000) { System.currentTimeMillis() }
        timerManager.confirm()

        assertTrue(timerManager.timerState.value.timeMillis == 0L)
        assertFalse(timerManager.timerState.value.visible)
    }

    @Test
    fun `reject updates timerState correctly`() = testScope.runTest  {
        val timerManager = TimerManager(this)
        timerManager.startTimer(System.currentTimeMillis(), 5000) { System.currentTimeMillis() }
        timerManager.reject()

        assertFalse(timerManager.timerState.value.visible)
    }

    @Test
    fun `cancelReject updates timerState correctly`() = testScope.runTest  {
        val timerManager = TimerManager(this)
        timerManager.startTimer(System.currentTimeMillis(), 5000) { System.currentTimeMillis() }
        timerManager.reject()
        timerManager.cancelReject()

        assertTrue(timerManager.timerState.value.visible)
    }

    @Test
    fun `reloadScreen with valid parameters restarts timer`() = testScope.runTest  {
        val timerManager = TimerManager(this)
        val currentTime = System.currentTimeMillis()
        timerManager.reloadScreen(currentTime, 5000)

        assertNotNull(timerManager.timerState.value)
        assertTrue(timerManager.timerState.value.visible)
    }

    @Test
    fun `reloadScreen with null parameters sets timerState correctly`() = testScope.runTest  {
        val timerManager = TimerManager(this)
        timerManager.reloadScreen(0, 5000)

        assertFalse(timerManager.timerState.value.visible)
        assertEquals(0, timerManager.timerState.value.timeMillis)
    }

    // Clean up after tests
    @After
    fun tearDown() {
        testScope.cancel()
    }
}
