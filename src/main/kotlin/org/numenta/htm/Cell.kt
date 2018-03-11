package org.numenta.htm

enum class Time {NOW, PAST}

class Cell(var isPredictive: Boolean = false, var isActive: Boolean = false, var isLearn: Boolean = false) {

    val toUpdate = ArrayList<Update>()

    private var isPastPredictive: Boolean = false
    private var isPastActive: Boolean = false
    private var isPastLearn: Boolean = false


    fun fixStates() {
        isPastPredictive = isPredictive
        isPastLearn = isLearn
        isPastActive = isActive

        isPredictive = false
        isLearn = false
        isActive = false
    }

    fun isActive(time: Time): Boolean = if (time == Time.NOW) isActive else isPastActive
    fun isLearn(time: Time): Boolean = if (time == Time.NOW) isLearn else isPastLearn
    fun isPredictive(time: Time): Boolean = if (time == Time.NOW) isPredictive else isPastPredictive

    val segments: MutableSet<Segment> = HashSet()

    fun getActiveSegment(time: Time): Segment = segments.find { it.isSegmentActive(time) && it.isSequenceSegment }
            ?: (segments.maxBy { it.realActivity(time) } ?: throw IllegalStateException())


    fun getBestMatchingSegment(time: Time, minThreshold: Int): Segment? = segments.filter { it.activity(time) >= minThreshold }.maxBy { it.activity(time) }

    fun adaptSegments(permanenceInc: Double, permanenceDec: Double, positiveReinforcement: Boolean) {
        toUpdate.forEach {
            if (positiveReinforcement) {
                it.segment.synapses.forEach {
                    it.permanence -= permanenceDec
                }
                it.adaptSynapses.forEach {
                    it.permanence += permanenceDec + permanenceInc
                }
            } else {
                it.adaptSynapses.forEach {
                    it.permanence -= permanenceDec
                }
            }
            it.segment.isSequenceSegment = it.isSequenceSegment
            it.segment.synapses.addAll(it.newSynapses)
            segments.add(it.segment)
        }
        toUpdate.clear()
    }

    class Update(val segment: Segment) {
        val adaptSynapses = ArrayList<InnerSynapse>()
        val newSynapses = ArrayList<InnerSynapse>()
        var isSequenceSegment: Boolean = false
    }

}