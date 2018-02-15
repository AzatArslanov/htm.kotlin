package org.numenta.htm

class SpatialPooling(private val field: Field) {
    var potentialPoolSize: Double = 0.85
    var minOverlap: Int = 0
    var connectedPermThreshold = 0.5
    var connectedPermInitialRange = 0.2
    var inhibitionRadius = 0
    var desiredLocalActivity = 2

    fun calcOverlap(input: Input) {
        field.columns.forEach {
            it.calcOverlap(input, minOverlap)
        }
    }

    fun doInhibition(input: Input) {
        field.columns.forEach {
            it.kthScore(field.calculateNeighbors(it, inhibitionRadius), desiredLocalActivity)
        }
    }


}