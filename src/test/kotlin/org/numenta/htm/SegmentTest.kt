package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class SegmentTest {

    @Test
    fun states() {
        val permanence = 0.5
        val segment = Segment(2, permanence - 0.1)

        assertFalse(segment.isSegmentActive(Time.NOW))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply {
            addState(Cell.States.ACTIVE)
            fixStates()
        }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply {
            addState(Cell.States.ACTIVE)
            fixStates() }))
        assertTrue(segment.isSegmentActive(Time.NOW))

        assertFalse(segment.isSegmentLearn(Time.NOW))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply {
            addState(Cell.States.LEARN)
            fixStates()
        }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply {
            addState(Cell.States.LEARN)
            fixStates()
        }))
        assertTrue(segment.isSegmentLearn(Time.NOW))
    }

}