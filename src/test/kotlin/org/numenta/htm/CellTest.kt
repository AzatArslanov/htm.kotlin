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
}