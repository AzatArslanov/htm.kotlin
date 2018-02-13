package org.numenta.htm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegionTest {

    @Test
    fun initialization() {
        val regionSize = 10
        val inputSize = 5
        val poolSize = 0.4
        val threshold = 0.5
        val initRange = 0.2
        val connectedSize = (inputSize * poolSize).toInt()

        val region = Region(regionSize, inputSize) {
            spatialPooling {
                potentialPoolSize = poolSize
                connectedPermThreshold = threshold
                connectedPermInitialRange = initRange
            }
        }


        assertEquals(regionSize, region.field.columns.size)
        region.field.columns.forEach {
            assertEquals(connectedSize, it.connectedSynapses.size)
            val downBound = threshold - initRange / 2.0
            val upperBound = threshold + initRange / 2.0
            it.connectedSynapses.forEach{
                assertTrue(it.permanence >= downBound)
                assertTrue(it.permanence < upperBound)
            }
        }
    }
}