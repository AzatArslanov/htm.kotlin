package org.numenta.htm

import java.util.*

class Column {
    private val dutyCycleLength = 1000
    private val overlapStates = StateRingBuffer(dutyCycleLength)
    private val activeStates = StateRingBuffer(dutyCycleLength)
    private var connectedPermThreshold = 0.0

    var boost: Int = 1
    var overlap: Int = 0
        private set

    private val synapses: MutableList<Synapse> = ArrayList()
    val connectedSynapses: List<Synapse> get() = synapses.filter { it.permanence >= connectedPermThreshold }
    val potentialSynapses: List<Synapse> get() = Collections.unmodifiableList(synapses)

    fun connectToInputField(inputs: List<Int>, connectedPermThreshold: Double, connectedPermInitialRange: Double) {
        this.connectedPermThreshold = connectedPermThreshold
        inputs.forEach {
            val initial = (connectedPermInitialRange * 100.0 * Math.random()) / 100.0
            val permanence = (connectedPermThreshold - connectedPermInitialRange / 2.0) + initial
            synapses.add(Synapse(permanence, it))
        }
    }

    fun overlap(input: Input, minOverlap: Int) {
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

    private fun kthScore(neighbors: List<Column>, desiredLocalActivity: Double): Int {
        if (neighbors.isEmpty()) {
            throw IllegalArgumentException("Neighbors is empty")
        }
        val sorted = neighbors.sortedByDescending { it.overlap }
        val index = ((sorted.size - 1) * desiredLocalActivity).toInt()
        return sorted[index].overlap
    }
}