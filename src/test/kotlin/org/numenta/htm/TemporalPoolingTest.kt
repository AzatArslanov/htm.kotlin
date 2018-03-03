package org.numenta.htm

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class TemporalPoolingTest {

    @Test
    fun getLearnCells() {
        val cell = Cell(false, false, true)
        val column = Mockito.mock(Column::class.java)
        `when`(column.cells).thenReturn(listOf(cell).toMutableList())
        val field = Mockito.mock(Field::class.java)
        `when`(field.columns).thenReturn(listOf(column))

        val temporalPooling = TemporalPooling(field)
        assertEquals(cell, temporalPooling.getLearnCells(Time.NOW)[0])
        assertEquals(0, temporalPooling.getLearnCells(Time.PAST).size)
    }

    @Test
    fun getSegmentActiveSynapses() {
        val cell = Cell(false, false, true)
        val column = Mockito.mock(Column::class.java)
        `when`(column.cells).thenReturn(listOf(cell).toMutableList())
        val field = Mockito.mock(Field::class.java)
        `when`(field.columns).thenReturn(listOf(column))

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
}