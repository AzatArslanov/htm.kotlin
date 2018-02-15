package org.numenta.htm

import org.junit.Test
import org.mockito.Mockito


import org.mockito.Mockito.*

class SpatialPoolingTest {

    @Test
    fun calcOverlap() {
        val first = mock(Column::class.java)
        val second = mock(Column::class.java)
        val field = mock(Field::class.java)
        `when`(field.columns).thenReturn(listOf(first, second))
        val spatialPooling = SpatialPooling(field)
        val input = Input(intArrayOf(1, 2, 3))

        spatialPooling.calcOverlap(input)

        verify(first, times(1)).calcOverlap(input, spatialPooling.minOverlap)
        verify(second, times(1)).calcOverlap(input, spatialPooling.minOverlap)
    }

    @Test
    fun doInhibition() {
        val first = mock(Column::class.java)
        val second = mock(Column::class.java)
        val field = mock(Field::class.java)
        val columns = listOf(first, second)
        `when`(field.columns).thenReturn(columns)
        `when`(field.calculateNeighbors(any(), anyInt())).thenReturn(columns)

        val spatialPooling = SpatialPooling(field)
        val input = Input(intArrayOf(1, 2, 3))

        spatialPooling.doInhibition(input)

        verify(first, times(1)).kthScore(columns, spatialPooling.desiredLocalActivity)
        verify(second, times(1)).kthScore(columns, spatialPooling.desiredLocalActivity)
        verify(field, times(2)).calculateNeighbors(any(), anyInt())
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}