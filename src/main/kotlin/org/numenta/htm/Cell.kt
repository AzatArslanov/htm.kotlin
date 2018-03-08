package org.numenta.htm

enum class Time {NOW, PAST}

class Cell(isPredictive: Boolean = false, isActive:Boolean = false, isLearn:Boolean = false) {
    private var isNowPredictive: Boolean = isPredictive
        private set(value) {
            isPastPredictive = isNowPredictive
            field = value
        }
    private var isNowActive: Boolean = isActive
        private set(value) {
            isPastActive = isNowActive
            field = value
        }
    private var isNowLearn: Boolean = isLearn
        private set(value) {
            isPastLearn = isNowLearn
            field = value
        }

    var toUpdate: Cell.Update? = null

    private var isPastPredictive: Boolean = false
    private var isPastActive: Boolean = false
    private var isPastLearn: Boolean = false

    private val lazyStates = HashSet<States>()

    fun addState(state: States) {
        lazyStates.add(state)
    }

    fun fixStates() {
        isNowPredictive = lazyStates.contains(States.PREDICTIVE)
        isNowLearn = lazyStates.contains(States.LEARN)
        isNowActive = lazyStates.contains(States.ACTIVE)
        lazyStates.clear()
    }

    fun isActive(time: Time): Boolean = if (time == Time.NOW) isNowActive else isPastActive
    fun isLearn(time: Time): Boolean = if (time == Time.NOW) isNowLearn else isPastLearn
    fun isPredictive(time: Time): Boolean = if (time == Time.NOW) isNowPredictive else isPastPredictive

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