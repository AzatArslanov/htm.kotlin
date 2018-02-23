package org.numenta.htm

class Segment(val activationThreshold: Int, val connectedPerm: Double) {
    val synapses: MutableList<InnerSynapse> = ArrayList()

    val isSegmentActive: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.isActive } >= activationThreshold

    val isSegmentLearn: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.isLearn } >= activationThreshold
}