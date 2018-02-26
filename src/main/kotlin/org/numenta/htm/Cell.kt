package org.numenta.htm

class Cell(var isPredictive: Boolean = false, var isActive: Boolean = false, var isLearn: Boolean = false) {

    val segments: MutableList<Segment> = ArrayList()

    fun getActiveSegment(): Segment = segments.find { it.isSegmentActive && it.isSequenceSegment } ?: (segments.maxBy { it.realActivity } ?: throw IllegalStateException())


    fun getBestMatchingSegment(minThreshold: Int): Segment? = segments.filter { it.activity >= minThreshold }.maxBy { it.activity }


}