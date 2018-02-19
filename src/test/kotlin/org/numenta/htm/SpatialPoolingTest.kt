package org.numenta.htm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito


import org.mockito.Mockito.*

class SpatialPoolingTest {

    @Test
    fun overlap() {
        val first = mock(Column::class.java)
        val second = mock(Column::class.java)
        val field = mock(Field::class.java)
        `when`(field.columns).thenReturn(listOf(first, second))
        val spatialPooling = SpatialPooling(field)
        val input = Input(intArrayOf(1, 2, 3))

        spatialPooling.overlap(input)

        verify(first, times(1)).overlap(input, spatialPooling.minOverlap)
        verify(second, times(1)).overlap(input, spatialPooling.minOverlap)
    }

    @Test
    fun inhibition() {
        val c1 = Column().apply {
            boost = 1
            connectToInputField(listOf(1, 2), 0.0, 0.0)
            overlap(Input(intArrayOf(1, 2)), 0)
        }

        val n1 = Column().apply {
            boost = 1
            connectToInputField(listOf(1), 0.0, 0.0)
            overlap(Input(intArrayOf(1)), 0)
        }
        val n2 = Column().apply {
            boost = 1
            connectToInputField(listOf(1, 2), 0.0, 0.0)
            overlap(Input(intArrayOf(1, 2)), 0)
        }
        val n3 = Column().apply {
            boost = 1
            connectToInputField(listOf(1, 2, 3), 0.0, 0.0)
            overlap(Input(intArrayOf(1, 2, 3)), 0)
        }

        val columns = listOf(c1)

        val neighbors = listOf(n1, n2, n3)

        val field = mock(Field::class.java)
        val spatialPooling = SpatialPooling(field).apply {
            desiredLocalActivity = 0.5
        }

        `when`(field.columns).thenReturn(columns)
        `when`(field.calculateNeighbors(c1, spatialPooling.inhibitionRadius)).thenReturn(neighbors)

        spatialPooling.inhibition()
        assertTrue(spatialPooling.activeColumns[0] == c1)
    }

    @Test
    fun learning() {
        val c1 = Column().apply {
            boost = 2
            connectToInputField(listOf(1), 0.0, 0.0)
            overlap(Input(intArrayOf(1)), 0)
        }
        val c2 = Column().apply {
            boost = 2
            connectToInputField(listOf(2), 0.0, 0.0)
            overlap(Input(intArrayOf(2)), 0)
        }
        val field = mock(Field::class.java)
        val spatialPooling = SpatialPooling(field).apply {
            desiredLocalActivity = 1.0
            permanenceInc = 0.5
            permanenceDec = 0.5
        }
        `when`(field.columns).thenReturn(listOf(c1,c2))
        `when`(field.calculateNeighbors(c1, spatialPooling.inhibitionRadius)).thenReturn(listOf(c2))
        `when`(field.calculateNeighbors(c2, spatialPooling.inhibitionRadius)).thenReturn(listOf(c1))

        spatialPooling.inhibition()
        spatialPooling.learning(Input(intArrayOf(1,2)))

        assertEquals(0.5, c1.potentialSynapses[0].permanence, 0.0)
        assertEquals(0.5, c2.potentialSynapses[0].permanence, 0.0)

        assertEquals(1, c1.boost)
        assertEquals(1, c2.boost)

        assertEquals(1, spatialPooling.inhibitionRadius)

    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}