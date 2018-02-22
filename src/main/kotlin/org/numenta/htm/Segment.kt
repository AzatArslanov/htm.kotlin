package org.numenta.htm

class Segment(private val activationThreshold: Int, private val connectedPerm: Double) {
    val synapses: MutableList<InnerSynapse> = ArrayList()

    val isSegmentActive: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.activeState } >= activationThreshold

    val isSegmentLearn: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.learnState } >= activationThreshold
}