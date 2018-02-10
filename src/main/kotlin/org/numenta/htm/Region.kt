package org.numenta.htm

class Region(size: Int, inputSize: Int) {
    val columns: MutableList<Column>

    init {
        columns = ArrayList(size)
        val countPotentialSynapses = (inputSize * Properties.potentialPoolSize).toInt()
        val intRange = IntRange(0, inputSize - 1)
        for (i in 0 until size) {
            val column = Column()
            intRange.shuffled().take(countPotentialSynapses).forEach {
                column.connectedSynapses.add(Synapse(0.0, it))
            }
            columns.add(column)
        }
    }
}