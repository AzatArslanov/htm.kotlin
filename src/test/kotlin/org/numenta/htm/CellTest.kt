package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell()
        assertEquals(false, cell.predictiveState)
        assertEquals(false, cell.learnState)
        assertEquals(false, cell.activeState)
    }

    @Test
    fun copy() {
        val cell = Cell(true, false, true).apply {
            segments.add(Segment(0, 0.0))
        }
        assertNull(cell.pastState)
        val pastState = cell.saveState()
        assertNotNull(cell.pastState)

        assertTrue(pastState.predictiveState)
        assertFalse(pastState.activeState)
        assertTrue(pastState.learnState)

        assertEquals(1, pastState.segments.size)
        assertNotEquals(System.identityHashCode(cell.segments), System.identityHashCode(pastState.segments))

    }
}