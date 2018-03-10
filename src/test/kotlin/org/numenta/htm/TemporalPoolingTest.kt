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
            synapses.add(InnerSynapse(permanence, Cell(isActive = true)))
            synapses.add(InnerSynapse(permanence, Cell(isActive = true)))
        }

        val toUpdate = temporalPooling.getSegmentActiveSynapses(Time.NOW, true, segment)
        assertEquals(segment, toUpdate.segment)
        assertEquals(2, toUpdate.adaptSynapses.size)
        assertEquals(1, toUpdate.newSynapses.size)

    }

    @Test
    fun process() {
        val threshold = 10
        val field = mock(Field::class.java)
        val activeColumn = mock(Column::class.java)
        val cell = mock(Cell::class.java)
        val segment = mock(Segment::class.java)
        val toUpdate = ArrayList<Cell.Update>()
        once(segment.isSequenceSegment).thenReturn(true)
        once(segment.isSegmentLearn(Time.PAST)).thenReturn(true)
        once(segment.isSegmentActive(Time.NOW)).thenReturn(true)
        once(cell.isPredictive(Time.PAST)).thenReturn(true)
        once(cell.getActiveSegment(Time.PAST)).thenReturn(segment)
        once(activeColumn.cells).thenReturn(listOf(cell).toMutableList())
        once(field.columns).thenReturn(listOf(activeColumn).toMutableList())
        once(cell.segments).thenReturn(listOf(segment).toMutableList())
        once(cell.toUpdate).thenReturn(toUpdate)
        once(cell.getBestMatchingSegment(Time.PAST, threshold)).thenReturn(segment)


        val temporalPooling = TemporalPooling(field).apply {
            minThreshold = threshold
        }

        temporalPooling.process(listOf(activeColumn))

        verify(cell, times(1)).isActive = true
        verify(cell, times(1)).isLearn = true
        verify(cell, times(1)).isPredictive = true
        verify(cell, times(1)).fixStates()
        verify(cell, times(1)).getBestMatchingSegment(Time.PAST, threshold)
        assertEquals(2, toUpdate.size)

        reset(segment)
        reset(cell)
        toUpdate.clear()

        once(segment.isSequenceSegment).thenReturn(false)
        once(activeColumn.getBestMatchingCell(Time.PAST, 10)).thenReturn(cell)
        once(cell.toUpdate).thenReturn(toUpdate)

        temporalPooling.process(listOf(activeColumn))
        verify(cell, times(1)).isActive =  true
        verify(cell, times(1)).isLearn =  true
        verify(cell, times(1)).fixStates()
        assertEquals(2, toUpdate.size)



    }
}