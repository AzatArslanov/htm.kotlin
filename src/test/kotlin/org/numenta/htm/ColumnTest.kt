package org.numenta.htm

import org.junit.Assert
import org.junit.Test

internal class ColumnTest {

    @Test
    fun overlap() {
        val column = Column().apply {
            boost = 1
            connectToInputField(listOf(1,2,3), 0.0, 0.0)
        }
        column.overlap(Input(intArrayOf(1, 2, 3)), 0)
        Assert.assertEquals(3, column.overlap)

        column.overlap(Input(intArrayOf(1, 2, 3)), 4)
        Assert.assertEquals(0, column.overlap)

        column.boost = 2
        column.overlap(Input(intArrayOf(1, 2, 3)), 0)
        Assert.assertEquals(6, column.overlap)
    }

    @Test
    fun connectToInputField() {
        val column = Column().apply {
            connectToInputField(listOf(1,2,3), 0.0, 0.0)
        }
        Assert.assertEquals(3, column.potentialSynapses.size)
        Assert.assertEquals(3, column.connectedSynapses.size)
    }
}