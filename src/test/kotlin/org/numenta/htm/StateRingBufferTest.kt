package org.numenta.htm

import org.junit.Test

import org.junit.Assert.*

class StateRingBufferTest {

    @Test
    fun countActiveState() {
        val buffer = StateRingBuffer(2).apply {
            addState(true)
            addState(false)
            addState(true)
            addState(false)
        }
        assertEquals(1, buffer.countActiveState())

    }

    @Test
    fun rate() {
        val buffer = StateRingBuffer(4).apply {
            addState(false)
            addState(true)
            addState(false)
            addState(false)
        }
        assertEquals(0.25, buffer.rate(), 0.0)
    }
}