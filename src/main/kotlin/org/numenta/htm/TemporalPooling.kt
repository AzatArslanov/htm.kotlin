package org.numenta.htm


class TemporalPooling(val field: Field) {

    fun saveFieldState() {
        field.columns.forEach {
            it.cells.forEach {
                it.copyWithoutSegments()
            }
        }

        field.columns.forEach {
            it.cells.forEach { cell ->
                val segments = ArrayList<Segment>()
                cell.segments.forEach {
                    val segment = Segment(it.activationThreshold, it.connectedPerm)
                    it.synapses.forEach {
                        segment.synapses.add(InnerSynapse(it.permanence, it.cell.pastState
                                ?: throw IllegalStateException()))
                    }
                    segments.add(segment)
                }
                cell.pastState?.segments?.addAll(segments)
            }
        }
    }
}

