package org.numenta.htm

class SpatialPooling(private val field: Field) {
    val activeColumns: MutableList<Column> = ArrayList()

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
        activeColumns.clear()
        field.columns.forEach {
            val neighbors = field.calculateNeighbors(it, inhibitionRadius)
            if (it.activate(neighbors, desiredLocalActivity)) {
                activeColumns.add(it)
            }
        }
    }

}