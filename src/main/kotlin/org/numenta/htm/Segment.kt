package org.numenta.htm

class Segment(private val activationThreshold: Int, private val connectedPerm: Double) {
    val synapses: MutableList<InnerSynapse> = ArrayList()

    val isSegmentActive: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.activeState } >= activationThreshold

    val isSegmentLearn: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.learnState } >= activationThreshold

    fun copy(): Segment {
        val segment = Segment(this.activationThreshold, this.connectedPerm)
        synapses.forEach {
            segment.synapses.add(it.copy())
        }
        return segment
    }
}