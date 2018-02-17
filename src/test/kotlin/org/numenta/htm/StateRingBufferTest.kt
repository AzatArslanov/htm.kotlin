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
        assertTrue(buffer.countActiveState() == 1)

    }
}