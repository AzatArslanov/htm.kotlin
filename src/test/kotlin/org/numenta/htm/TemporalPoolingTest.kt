package org.numenta.htm

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as once

class TemporalPoolingTest : MockitoTest() {

    @Test
    fun getLearnCells() {
        val cell = Cell(false, false, true)
        val column = mock(Column::class.java)
        once(column.cells).thenReturn(listOf(cell).toMutableList())
        val field = mock(Field::class.java)
        once(field.columns).thenReturn(listOf(column))

        val temporalPooling = TemporalPooling(field)
        assertEquals(cell, temporalPooling.getLearnCells(Time.NOW)[0])
        assertEquals(0, temporalPooling.getLearnCells(Time.PAST).size)
    }

    @Test
    fun getSegmentActiveSynapses() {
        val cell = Cell(false, false, true)
        val column = mock(Column::class.java)
        once(column.cells).thenReturn(listOf(cell).toMutableList())
        val field = mock(Field::class.java)
        once(field.columns).thenReturn(listOf(column))

        val temporalPooling = TemporalPooling(field).apply {
            newSynapsesCount = 10
        }
        val permanence = 0.0
        val segment = Segment(0, permanence).apply {
            synapses.add(InnerSynapse(permanence, Cell().apply { isNowActive = true }))
            synapses.add(InnerSynapse(permanence, Cell().apply { isNowActive = true }))
        }

        val toUpdate = temporalPooling.getSegmentActiveSynapses(Time.NOW, true, segment)
        assertEquals(segment, toUpdate.segment)
        assertEquals(2, toUpdate.adaptSynapses.size)
        assertEquals(1, toUpdate.newSynapses.size)

    }

    @Test
    fun process() {
        val field = mock(Field::class.java)

        val temporalPooling = TemporalPooling(field).apply {
            minThreshold = 10
        }

        val activeColumn = mock(Column::class.java)
        val cell = mock(Cell::class.java)
        val segment = mock(Segment::class.java)
        once(segment.isSequenceSegment).thenReturn(true)
        once(segment.isSegmentLearn(Time.PAST)).thenReturn(true)
        once(cell.isPredictive(Time.PAST)).thenReturn(true)
        once(cell.getActiveSegment(Time.PAST)).thenReturn(segment)
        once(activeColumn.cells).thenReturn(listOf(cell).toMutableList())

        temporalPooling.process(listOf(activeColumn))

        verify(cell, times(1)).isNowActive = true
        verify(cell, times(1)).isNowLearn = true

        reset(segment)
        reset(cell)

        once(segment.isSequenceSegment).thenReturn(false)
        once(activeColumn.getBestMatchingCell(Time.PAST, 10)).thenReturn(cell)

        temporalPooling.process(listOf(activeColumn))
        verify(cell, times(1)).isNowActive = true
        verify(cell, times(1)).isNowLearn = true
        verify(cell, times(1)).toUpdate = any()

    }
}