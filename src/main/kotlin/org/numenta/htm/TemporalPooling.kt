package org.numenta.htm


class TemporalPooling(val field: Field) {
    var activationThreshold = 0
    var minThreshold = 0
    var connectedPerm = 0.0
    var newSynapsesCount = 0
    var initialPerm = 0.0


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

    fun process(activeColumns: List<Column>) {
        fixStates()
        firstPhase(activeColumns)
        secondPhase()
    }

    private fun secondPhase() {
        field.columns.forEach {
            it.cells.forEach { cell ->
                cell.segments.forEach { segment ->
                    if (segment.isSegmentActive(Time.NOW)) {
                        cell.isPredictive = true
                        val update = getSegmentActiveSynapses(Time.NOW, false, segment)
                        cell.toUpdate.add(update)
                    }
                }
                val predUpdate = cell.getBestMatchingSegment(Time.PAST, minThreshold)
                val update = getSegmentActiveSynapses(Time.PAST, true, predUpdate)
                cell.toUpdate.add(update)
            }
        }
    }

    private fun firstPhase(activeColumns: List<Column>) {
        activeColumns.forEach { column ->
            var buPredicted = false
            var lcChosen = false
            column.cells.forEach {cell ->
                if (cell.isPredictive(Time.PAST)) {
                    val activeSegment = cell.getActiveSegment(Time.PAST)
                    if (activeSegment.isSequenceSegment) {
                        buPredicted = true
                        cell.isActive = true
                        if (activeSegment.isSegmentLearn(Time.PAST)) {
                            lcChosen = true
                            cell.isLearn = true
                        }
                    }
                }
            }

            if (!buPredicted) {
                column.cells.forEach {
                    it.isActive = true
                }
            }

            if (!lcChosen) {
                val cell = column.getBestMatchingCell(Time.PAST, minThreshold)
                cell.isLearn = true
                val update = getSegmentActiveSynapses(Time.PAST, true, null)
                update.isSequenceSegment = true
                cell.toUpdate.add(update)
            }
        }
    }

    private fun fixStates() {
        field.columns.forEach {
            it.cells.forEach {
                it.fixStates()
            }
        }
    }
}

