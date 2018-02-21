package org.numenta.htm

class Cell {
    var predictiveState = false
        set(value) {
            previousPredictiveState = predictiveState
            field = value
        }
    var previousPredictiveState = false
        private set

    var activeSctate = false
        set(value) {
            previousActiveState = activeSctate
            field = value
        }
    var previousActiveState = false
        private set

    var learnState = false
        set(value) {
            previousLearnState = learnState
            field = value
        }
    var previousLearnState = false
        private set

    private val segments: MutableList<Segment> = ArrayList()

}