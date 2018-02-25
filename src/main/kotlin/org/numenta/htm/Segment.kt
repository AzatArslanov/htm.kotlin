package org.numenta.htm

class Segment(val activationThreshold: Int, val connectedPerm: Double) {
    var isSequenceSegment = false

    val synapses: MutableList<InnerSynapse> = ArrayList()

    val isSegmentActive: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.isActive } >= activationThreshold

    val realActivity: Int get() = synapses.count { it.permanence >= connectedPerm && it.cell.isActive }

    val activity: Int get() = synapses.count { it.cell.isActive }

    val isSegmentLearn: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.isLearn } >= activationThreshold
}