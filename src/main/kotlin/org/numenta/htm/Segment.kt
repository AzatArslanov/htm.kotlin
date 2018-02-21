package org.numenta.htm

class Segment {
    val synapses: MutableList<InnerSynapse> = ArrayList()

    fun copy(): Segment {
        val segment = Segment()
        synapses.forEach {
            segment.synapses.add(it.copy())
        }
        return segment
    }
}