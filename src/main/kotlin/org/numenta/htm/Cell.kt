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

    val segments: MutableList<Segment> = ArrayList()


}