package org.numenta.htm

class SpatialPooling(private val field: Field) {
    val activeColumns: MutableList<Column> = ArrayList()

    var potentialPoolSize: Double = 0.85
    var minOverlap: Int = 0
    var connectedPermThreshold = 0.5
    var connectedPermInitialRange = 0.2
    var inhibitionRadius = 0
    var desiredLocalActivity = 0.3
    var permanenceInc = 0.0
    var permanenceDec = 0.0

    fun overlap(input: Input) {
        field.columns.forEach {
            it.overlap(input, minOverlap)
        }
    }

    fun inhibition() {
        activeColumns.clear()
        field.columns.forEach {
            val neighbors = field.calculateNeighbors(it, inhibitionRadius)
            if (it.activate(neighbors, desiredLocalActivity)) {
                activeColumns.add(it)
            }
        }
    }

    fun learning(input: Input) {
        activeColumns.forEach {
            it.potentialSynapses.forEach {
                if (input.contains(it.input)) {
                    it.permanence += permanenceInc
                    it.permanence = Math.min(1.0, it.permanence)
                } else {
                  it.permanence -= permanenceDec
                    it.permanence = Math.max(0.0, it.permanence)
                }
            }
        }
        field.columns.forEach {
            val neighbors = field.calculateNeighbors(it, inhibitionRadius)
            val minDutyCycle = 0.01 * maxDutyCycle(neighbors)
            it.boostFunction(minDutyCycle)
            it.increasePermanence(minDutyCycle, 0.1 * connectedPermThreshold)
        }
        inhibitionRadius = averageReceptiveFieldSize()
    }

    private fun averageReceptiveFieldSize(): Int {
        val sum = field.columns.sumBy { it.connectedSynapses.size }
        return sum / field.columns.size
    }

    private fun maxDutyCycle(neighbors: List<Column>): Double = neighbors.maxWith(compareBy { it.activeDutyCycle() })?.activeDutyCycle() ?: 0.0

}