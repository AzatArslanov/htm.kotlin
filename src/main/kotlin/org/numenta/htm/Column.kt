package org.numenta.htm

class Column {
    private val dutyCycleLength = 1000
    private val overlapStates = StateRingBuffer(dutyCycleLength)
    private val activeStates = StateRingBuffer(dutyCycleLength)

    var boost: Int = 0
    var overlap: Int = 0

    val connectedSynapses: MutableList<Synapse> = ArrayList()

    fun calcOverlap(input: Input, minOverlap: Int) {
        overlap = 0
        connectedSynapses.forEach {
            if (input.contains(it.input)) {
                overlap += 1
            }
        }
        if (overlap < minOverlap) {
            overlap = 0
            overlapStates.addState(false)
        } else {
            overlap *= boost
            overlapStates.addState(true)
        }
    }

    fun activate(neighbors: List<Column>, desiredLocalActivity: Double): Boolean {
        val minLocalActivity = kthScore(neighbors, desiredLocalActivity)
        if (overlap > 0 && overlap >= minLocalActivity) {
            activeStates.addState(true)
            return true
        }
        activeStates.addState(false)
        return false
    }

    private fun kthScore(neighbors: List<Column>, desiredLocalActivity: Double) : Int {
        if (neighbors.isEmpty()) {
            throw IllegalArgumentException("Neighbors is empty")
        }
        val sorted = neighbors.sortedByDescending {  it.overlap }
        val index = ((sorted.size - 1) * desiredLocalActivity).toInt()
        return sorted[index].overlap
    }
}