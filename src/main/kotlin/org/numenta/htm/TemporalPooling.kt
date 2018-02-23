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
                        val pastState = it.cell.pastState
                        if (pastState != null) {
                            segment.synapses.add(InnerSynapse(it.permanence, pastState))
                        }
                    }
                    segments.add(segment)
                }
                cell.pastState?.segments?.addAll(segments)
            }
        }
    }
}

