package org.numenta.htm


class TemporalPooling(val field: Field) {
    var activationThreshold = 0
    var minThreshold = 0
    var connectedPerm = 0.0
    var newSynapsesCount = 0
    var initialPerm = 0.0

    private val toFixState = ArrayList<FixState>()

    private class FixState(val cell: Cell, val state: State) {
        enum class State {ACTIVE, LEARN, PREDICT}
    }


    fun getLearnCells(time: Time):List<Cell> {
        val result = ArrayList<Cell>()
        field.columns.forEach {
            result.addAll(it.cells.filter { it.isLearn(time) })
        }
        return result
    }

    fun getSegmentActiveSynapses(time: Time, isNewSynapses: Boolean, segment: Segment? = null): Cell.Update {
        val toUpdate = Cell.Update(segment ?: Segment(activationThreshold, connectedPerm))
        if (segment != null) {
            toUpdate.adaptSynapses.addAll(segment.getActiveSynapses(time))
        }
        if (isNewSynapses) {
            val count = newSynapsesCount - toUpdate.adaptSynapses.size
            if (count > 0) {
                getLearnCells(time).shuffled().take(count).forEach {
                    toUpdate.newSynapses.add(InnerSynapse(initialPerm, it))
                }
            }
        }
        return toUpdate
    }

    private fun fixStates() {
        toFixState.forEach {
            when (it.state) {
                FixState.State.ACTIVE -> it.cell.isNowActive = true
                FixState.State.LEARN -> it.cell.isNowLearn = true
                FixState.State.PREDICT -> it.cell.isNowPredictive = true
            }
        }
        toFixState.clear()
    }

    fun process(activeColumns: List<Column>) {
        activeColumns.forEach { column ->
            var buPredicted = false
            var lcChosen = false
            column.cells.forEach {cell ->
                if (cell.isPredictive(Time.PAST)) {
                    val activeSegment = cell.getActiveSegment(Time.PAST)
                    if (activeSegment.isSequenceSegment) {
                        buPredicted = true
                        toFixState.add(FixState(cell, FixState.State.ACTIVE))
                        if (activeSegment.isSegmentLearn(Time.PAST)) {
                            lcChosen = true
                            toFixState.add(FixState(cell, FixState.State.LEARN))
                        }
                    }
                }
            }

            if (!buPredicted) {
                column.cells.forEach {
                    toFixState.add(FixState(it, FixState.State.ACTIVE))
                }
            }

            if (!lcChosen) {
                val cell = column.getBestMatchingCell(Time.PAST, minThreshold)
                toFixState.add(FixState(cell, FixState.State.LEARN))
                val update = getSegmentActiveSynapses(Time.PAST, true, null)
                update.isSequenceSegment = true
                cell.toUpdate = update
            }
        }
        fixStates()
    }
}

