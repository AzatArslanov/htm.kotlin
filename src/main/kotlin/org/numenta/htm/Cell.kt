package org.numenta.htm

enum class Time {NOW, PAST}

class Cell(isPredictive: Boolean = false, isActive:Boolean = false, isLearn:Boolean = false) {
    var isNowPredictive: Boolean = isPredictive
        set(value) {
            isPastPredictive = isNowPredictive
            field = value
        }
    var isNowActive: Boolean = isActive
        set(value) {
            isPastActive = isNowActive
            field = value
        }
    var isNowLearn: Boolean = isLearn
        set(value) {
            isPastLearn = isNowLearn
            field = value
        }

    var toUpdate: Cell.Update? = null

    private var isPastPredictive: Boolean = false
    private var isPastActive: Boolean = false
    private var isPastLearn: Boolean = false

    fun isActive(time: Time): Boolean = if (time == Time.NOW) isNowActive else isPastActive
    fun isLearn(time: Time): Boolean = if (time == Time.NOW) isNowLearn else isPastLearn
    fun isPredictive(time: Time): Boolean = if (time == Time.NOW) isNowPredictive else isPastPredictive

    val segments: MutableList<Segment> = ArrayList()

    fun getActiveSegment(time: Time): Segment = segments.find { it.isSegmentActive(time) && it.isSequenceSegment }
            ?: (segments.maxBy { it.realActivity(time) } ?: throw IllegalStateException())


    fun getBestMatchingSegment(time: Time, minThreshold: Int): Segment? = segments.filter { it.activity(time) >= minThreshold }.maxBy { it.activity(time) }

    class Update(val sequenceSegment: Boolean, val segment: Segment, val synapses: List<InnerSynapse>)

}