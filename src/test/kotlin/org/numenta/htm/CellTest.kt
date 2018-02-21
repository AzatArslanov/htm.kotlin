package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell()
        assertEquals(false, cell.predictiveState)
        assertEquals(false, cell.previousPredictiveState)

        cell.predictiveState = true
        cell.predictiveState = true

        assertEquals(true, cell.predictiveState)
        assertEquals(true, cell.previousPredictiveState)

    }
}