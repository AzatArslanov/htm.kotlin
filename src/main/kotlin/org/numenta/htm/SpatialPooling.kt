package org.numenta.htm

class SpatialPooling(private val field: Field) {
    var potentialPoolSize: Double = 0.85
    var minOverlap: Int = 0
    var connectedPermThreshold = 0.5
    var connectedPermInitialRange = 0.2
    var inhibitionRadius = 0
    var desiredLocalActivity = 0.3

    fun calcOverlap(input: Input) {
        field.columns.forEach {
            it.calcOverlap(input, minOverlap)
        }
    }

    fun doInhibition() {
        field.columns.forEach {
            val minLocalActivity = kthScore(field.calculateNeighbors(it, inhibitionRadius))
            if (it.overlap > 0 && it.overlap >= minLocalActivity) {
                field.activeColumns.add(it)
            }
        }
    }

    private fun kthScore(neighbors: List<Column>) : Int {
        if (neighbors.isEmpty()) {
            throw IllegalArgumentException("Neighbors is empty")
        }
        val sorted = neighbors.sortedByDescending {  it.overlap }
        val index = ((sorted.size - 1) * desiredLocalActivity).toInt()
        return sorted[index].overlap
    }

}