package org.numenta.htm

class Cell(isPredictive: Boolean = false, isActive:Boolean = false, isLearn:Boolean = false) {
    var isPredictive: Boolean = isPredictive
        set(value) {
            isPastPredictive = isPredictive
            field = value
        }
    var isActive: Boolean = isActive
        set(value) {
            isPastActive = isActive
            field = value
        }
    var isLearn: Boolean = isLearn
        set(value) {
            isPastLearn = isLearn
            field = value
        }

    var isPastPredictive: Boolean = false
        private set
    var isPastActive: Boolean = false
        private set
    var isPastLearn: Boolean = false
        private set

    val segments: MutableList<Segment> = ArrayList()

    fun getActiveSegment(): Segment = segments.find { it.isSegmentActive && it.isSequenceSegment }
            ?: (segments.maxBy { it.realActivity } ?: throw IllegalStateException())


    fun getBestMatchingSegment(minThreshold: Int): Segment? = segments.filter { it.activity >= minThreshold }.maxBy { it.activity }

}