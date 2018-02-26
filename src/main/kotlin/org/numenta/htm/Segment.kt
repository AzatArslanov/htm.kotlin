package org.numenta.htm

class Segment(val activationThreshold: Int, val connectedPerm: Double) {
    var isSequenceSegment = false

    val synapses: MutableList<InnerSynapse> = ArrayList()

    fun isSegmentActive(time: Time): Boolean = synapses.count { it.permanence >= connectedPerm && it.cell.isActive(time) } >= activationThreshold

    fun realActivity(time: Time): Int = synapses.count { it.permanence >= connectedPerm && it.cell.isActive(time) }

    val activity: Int get() = synapses.count { it.cell.isNowActive }

    val isSegmentLearn: Boolean get() = synapses.count { it.permanence >= connectedPerm && it.cell.isNowLearn } >= activationThreshold
}