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

    fun doInhibition() {
        field.columns.forEach {
            val minLocalActivity = kthScore(field.calculateNeighbors(it, inhibitionRadius))
            if (it.overlap > 0 && it.overlap >= minLocalActivity) {
                field.activeColumns.add(it)
            }
        }
    }

    private fun kthScore(neighbors: List<Column>) : Int {
        val sortedWith = neighbors.sortedWith(compareBy({ it.overlap }))
        return if (sortedWith.size > desiredLocalActivity) sortedWith[desiredLocalActivity-1].overlap else sortedWith.last().overlap
    }

}