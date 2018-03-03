package org.numenta.htm


class TemporalPooling(val field: Field) {
    var activationThreshold = 0
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
}

