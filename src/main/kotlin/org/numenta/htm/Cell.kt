package org.numenta.htm

class Cell(var isPredictive:Boolean = false, var isActive:Boolean = false, var isLearn:Boolean = false) {

    val segments: MutableList<Segment> = ArrayList()

    var pastState: Cell? = null
        private set

    fun copyWithoutSegments(): Cell {
        val cell = Cell(this.isPredictive, this.isActive, this.isLearn)
        pastState = cell
        return cell
    }
}