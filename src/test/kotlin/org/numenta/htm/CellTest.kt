package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell()
        assertEquals(false, cell.predictiveState)
        assertEquals(false, cell.previousPredictiveState)

        assertEquals(false, cell.learnState)
        assertEquals(false, cell.previousLearnState)

        assertEquals(false, cell.activeSctate)
        assertEquals(false, cell.previousActiveState)

        cell.predictiveState = true
        cell.predictiveState = true

        assertEquals(true, cell.predictiveState)
        assertEquals(true, cell.previousPredictiveState)

        cell.learnState = true
        cell.learnState = true

        assertEquals(true, cell.learnState)
        assertEquals(true, cell.previousLearnState)

        cell.activeSctate = true
        cell.activeSctate = true

        assertEquals(true, cell.activeSctate)
        assertEquals(true, cell.previousActiveState)
    }
}