package org.numenta.htm

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

internal class ColumnTest {

    @Test
    fun overlap() {
        val column = Column().apply {
            boost = 1
            connectToInputField(listOf(1,2,3), 0.0, 0.0)
        }
        column.overlap(Input(intArrayOf(1, 2, 3)), 0)
        assertEquals(3, column.overlap)

        column.overlap(Input(intArrayOf(1, 2, 3)), 4)
        assertEquals(0, column.overlap)

        column.boost = 2
        column.overlap(Input(intArrayOf(1, 2, 3)), 0)
        assertEquals(6, column.overlap)
    }

    @Test
    fun connectToInputField() {
        val column = Column().apply {
            connectToInputField(listOf(1,2,3), 0.0, 0.0)
        }
        assertEquals(3, column.potentialSynapses.size)
        assertEquals(3, column.connectedSynapses.size)
    }

    @Test
    fun getBestMatchingCell() {
        val one = mock(Cell::class.java)
        `when`(one.getBestMatchingSegment(anyInt())).thenReturn(Segment(0, 0.0))
        val two = Cell().apply {
            segments.add(Segment(0, 0.0))
            segments.add(Segment(0, 0.0))
        }
        val three = Cell().apply {
            segments.add(Segment(0, 0.0))
            segments.add(Segment(0, 0.0))
            segments.add(Segment(0, 0.0))
        }
        val column = Column().apply {
            cells.add(one)
            cells.add(two)
        }
        assertEquals(one, column.getBestMatchingCell(0))

        column.apply {
            cells.clear()
            cells.add(two)
            cells.add(three)
        }
        assertEquals(two, column.getBestMatchingCell(0))
    }
}