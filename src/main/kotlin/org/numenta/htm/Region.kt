package org.numenta.htm

import java.util.*

class Region(size: Int, inputSize: Int) {
    val columns: MutableList<Column>

    init {
        columns = ArrayList(size)
        val countPotentialSynapses = (inputSize * Properties.potentialPoolSize).toInt()
        val intRange = IntRange(0, inputSize - 1)
        for (i in 0 until size) {
            val column = Column()
            intRange.shuffled().take(countPotentialSynapses).forEach {
                column.connectedSynapses.add(Synapse(generateInitialPermanence(), it))
            }
            columns.add(column)
        }
    }

    private fun generateInitialPermanence() : Double {
        val initial = (Properties.connectedPermInitialRange * 100.0 * Math.random()) / 100.0
        return (Properties.connectedPermThreshold - Properties.connectedPermInitialRange / 2.0) + initial
    }
}