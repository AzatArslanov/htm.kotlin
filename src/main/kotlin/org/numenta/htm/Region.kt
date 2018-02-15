package org.numenta.htm

import java.util.*

class Region(size: Int, inputSize: Int, init: Region.() -> Unit) {
    val field = Field(size)

    private var sp: SpatialPooling = SpatialPooling(field)

    init {
        init()
        val countPotentialSynapses = (inputSize * sp.potentialPoolSize).toInt()
        val intRange = IntRange(0, inputSize - 1)
        field.columns.forEach { column ->
            intRange.shuffled().take(countPotentialSynapses).forEach {
                column.connectedSynapses.add(Synapse(generateInitialPermanence(), it))
            }
        }
    }

    fun process(input: Input) {
        //Phase #1: Overlap
        sp.calcOverlap(input)
        //Phase #2: Inhibition
        sp.doInhibition(input)
    }

    fun spatialPooling(init: SpatialPooling.() -> Unit) {
        val spatialPooling = SpatialPooling(field)
        spatialPooling.init()
        this.sp = spatialPooling
    }

    private fun generateInitialPermanence() : Double {
        val initial = (sp.connectedPermInitialRange * 100.0 * Math.random()) / 100.0
        return (sp.connectedPermThreshold - sp.connectedPermInitialRange / 2.0) + initial
    }
}
