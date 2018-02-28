package org.numenta.htm

class Segment(private val activationThreshold: Int, private val connectedPerm: Double) {
    var isSequenceSegment = false

    val synapses: MutableList<InnerSynapse> = ArrayList()

    fun isSegmentActive(time: Time): Boolean = synapses.count { it.permanence >= connectedPerm && it.cell.isActive(time) } >= activationThreshold

    fun realActivity(time: Time): Int = synapses.count { it.permanence >= connectedPerm && it.cell.isActive(time) }

    fun activity(time: Time): Int = synapses.count { it.cell.isActive(time) }

    fun isSegmentLearn(time: Time): Boolean = synapses.count { it.permanence >= connectedPerm && it.cell.isLearn(time) } >= activationThreshold
}