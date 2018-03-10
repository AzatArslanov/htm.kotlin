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

    val segments: MutableList<Segment> = ArrayList()

    fun getActiveSegment(time: Time): Segment = segments.find { it.isSegmentActive(time) && it.isSequenceSegment }
            ?: (segments.maxBy { it.realActivity(time) } ?: throw IllegalStateException())


    fun getBestMatchingSegment(time: Time, minThreshold: Int): Segment? = segments.filter { it.activity(time) >= minThreshold }.maxBy { it.activity(time) }

    class Update(val segment: Segment) {
        val adaptSynapses = ArrayList<InnerSynapse>()
        val newSynapses = ArrayList<InnerSynapse>()
        var isSequenceSegment: Boolean = false
    }

    enum class States {PREDICTIVE, ACTIVE, LEARN}

}