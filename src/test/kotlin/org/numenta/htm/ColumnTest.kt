package org.numenta.htm

import org.junit.Assert
import org.junit.Test

internal class ColumnTest {

    @Test
    fun calcOverlap() {
        val column = Column()
        val minOverlap = 0
        column.boost = 1
        column.connectedSynapses.add(Synapse(0.0, 1))
        column.calcOverlap(Input(intArrayOf(1)), minOverlap)
        Assert.assertEquals(1, column.overlap)

        column.boost = 2
        column.calcOverlap(Input(intArrayOf(1)), minOverlap)
        Assert.assertEquals(2, column.overlap)

        column.boost = 0
        column.calcOverlap(Input(intArrayOf(1)), minOverlap)
        Assert.assertEquals(0, column.overlap)
    }
}