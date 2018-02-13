package org.numenta.htm

class Column {
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
        } else {
            overlap *= boost
        }
    }
}