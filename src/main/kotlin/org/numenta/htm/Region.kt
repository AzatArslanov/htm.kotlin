package org.numenta.htm

import java.util.*

class Region(size: Int, inputSize: Int, init: Region.() -> Unit) {
    val columns: MutableList<Column>

    private var sp: SpatialPooling = SpatialPooling()

    init {
        init()
        columns = ArrayList(size)
        val countPotentialSynapses = (inputSize * sp.potentialPoolSize).toInt()
        val intRange = IntRange(0, inputSize - 1)
        for (i in 0 until size) {
            val column = Column()
            intRange.shuffled().take(countPotentialSynapses).forEach {
                column.connectedSynapses.add(Synapse(generateInitialPermanence(), it))
            }
            columns.add(column)
        }
    }

    fun spatialPooling(init: SpatialPooling.() -> Unit) {
        val spatialPooling = SpatialPooling()
        spatialPooling.init()
        this.sp = spatialPooling
    }

    private fun generateInitialPermanence() : Double {
        val initial = (sp.connectedPermInitialRange * 100.0 * Math.random()) / 100.0
        return (sp.connectedPermThreshold - sp.connectedPermInitialRange / 2.0) + initial
    }

    inner class SpatialPooling {
        var potentialPoolSize: Double = 0.85
        var minOverlap: Int = 0
        var connectedPermThreshold = 0.5
        var connectedPermInitialRange = 0.2

        private fun calcOverlap(input: Input) {
            columns.forEach {
                it.calcOverlap(input, sp.minOverlap)
            }
        }
    }
}
