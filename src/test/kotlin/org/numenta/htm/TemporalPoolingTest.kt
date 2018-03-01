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
}