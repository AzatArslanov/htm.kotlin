package org.numenta.htm

class Cell(var predictiveState:Boolean = false, var activeState:Boolean = false, var learnState:Boolean = false) {

    val segments: MutableList<Segment> = ArrayList()

    var pastState: Cell? = null
        private set

    fun saveState(): Cell {
        val cell = copyWithoutSegments()
        segments.forEach {
            cell.segments.add(it.copy())
        }
        pastState = cell
        return cell
    }

    fun copyWithoutSegments(): Cell = Cell(this.predictiveState, this.activeState, this.learnState)
}