package org.numenta.htm

import org.junit.Assert.assertTrue
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
        val c1 = Column().apply { overlap = 2 }
        val n1 = Column().apply { overlap = 1 }
        val n2 = Column().apply { overlap = 2 }
        val n3 = Column().apply { overlap = 3 }

        val columns = listOf(c1)

        val neighbors = listOf(n1, n2, n3)

        val field = mock(Field::class.java)
        val spatialPooling = SpatialPooling(field).apply {
            desiredLocalActivity = 0.5
        }

        `when`(field.columns).thenReturn(columns)
        `when`(field.calculateNeighbors(c1, spatialPooling.inhibitionRadius)).thenReturn(neighbors)

        spatialPooling.doInhibition()
        assertTrue(spatialPooling.activeColumns[0] == c1)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}