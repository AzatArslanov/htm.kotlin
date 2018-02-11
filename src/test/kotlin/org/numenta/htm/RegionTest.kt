package org.numenta.htm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegionTest {

    @Test
    fun initialization() {
        val regionSize = 10
        val inputSize = 5
        val connectedSize = (inputSize * Properties.potentialPoolSize).toInt()
        val region = Region(regionSize, inputSize)
        assertEquals(regionSize, region.columns.size)
        region.columns.forEach {
            assertEquals(connectedSize, it.connectedSynapses.size)
            val downBound = Properties.connectedPermThreshold - Properties.connectedPermInitialRange / 2.0
            val upperBound = Properties.connectedPermThreshold + Properties.connectedPermInitialRange / 2.0
            it.connectedSynapses.forEach{
                assertTrue(it.permanence >= downBound)
                assertTrue(it.permanence < upperBound)
            }
        }
    }
}