package org.numenta.htm

class Region(size: Int, inputSize: Int, init: Region.() -> Unit) {
    val field = Field(size)

    private var sp: SpatialPooling = SpatialPooling(field)

    init {
        init()
        val countPotentialSynapses = (inputSize * sp.potentialPoolSize).toInt()
        val intRange = IntRange(0, inputSize - 1)
        field.columns.forEach {
            it.connectToInputField(intRange.shuffled().take(countPotentialSynapses), sp.connectedPermThreshold, sp.connectedPermInitialRange)
        }
    }

    fun process(input: Input) {
        //Phase #1: Overlap
        sp.overlap(input)
        //Phase #2: Inhibition
        sp.inhibition()
    }

    fun spatialPooling(init: SpatialPooling.() -> Unit) {
        val spatialPooling = SpatialPooling(field)
        spatialPooling.init()
        this.sp = spatialPooling
    }
}
