package org.numenta.htm

import java.util.*
import kotlin.collections.ArrayList

class Column(cellsPerColumn: Int = 0) {
    val cells: MutableList<Cell> = ArrayList(cellsPerColumn)

    init {
        for (i in 0 until cellsPerColumn) {
            cells.add(Cell())
        }
    }

    private val dutyCycleLength = 1000
    private val overlapStates = StateRingBuffer(dutyCycleLength)
    private val activeStates = StateRingBuffer(dutyCycleLength)
    private var connectedPermThreshold = 0.0

    var boost: Int = 1
        set(value) = if (value < 1) throw IllegalArgumentException("boost must be more then 1") else field = value
    var overlap: Int = 0
        private set

    private val synapses: MutableList<Synapse> = ArrayList()
    val connectedSynapses: List<Synapse> get() = synapses.filter { it.permanence >= connectedPermThreshold }
    val potentialSynapses: List<Synapse> get() = Collections.unmodifiableList(synapses)

    fun getBestMatchingCell(time: Time, minThreshold: Int): Cell = cells.find { it.getBestMatchingSegment(time, minThreshold) != null }
            ?: (cells.minBy { it.segments.size } ?: throw IllegalStateException())

    fun connectToInputField(inputs: List<Int>, connectedPermThreshold: Double, connectedPermInitialRange: Double) {
        this.connectedPermThreshold = connectedPermThreshold
        inputs.forEach {
            val initial = (connectedPermInitialRange * 100.0 * Math.random()) / 100.0
            val permanence = (connectedPermThreshold - connectedPermInitialRange / 2.0) + initial
            synapses.add(Synapse(permanence, it))
        }
    }

    fun boostFunction(minDutyCycle: Double) {
        if (activeDutyCycle() > minDutyCycle) {
            boost = 1
        } else {
            boost += 1
        }
    }

    fun increasePermanence(minDutyCycle: Double, increment: Double) {
        if (overlapDutyCycle() < minDutyCycle) {
            synapses.forEach {
                it.permanence += increment
                it.permanence = Math.min(1.0, it.permanence)
            }
        }
    }

    fun activeDutyCycle(): Double = activeStates.rate()

    fun overlapDutyCycle(): Double = overlapStates.rate()

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