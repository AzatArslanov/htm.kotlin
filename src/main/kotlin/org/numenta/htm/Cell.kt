package org.numenta.htm

class Cell {
    var predictiveState = false
    var activeState = false
    var learnState = false

    private val segments: MutableList<Segment> = ArrayList()

}