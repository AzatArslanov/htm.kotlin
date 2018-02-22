package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class SegmentTest {

    @Test
    fun states() {
        val permanence = 0.5
        val segment = Segment(2, permanence - 0.1)

        assertFalse(segment.isSegmentActive)
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { activeState = true }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { activeState = true }))
        assertTrue(segment.isSegmentActive)

        assertFalse(segment.isSegmentLearn)
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { learnState = true }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { learnState = true }))
        assertTrue(segment.isSegmentLearn)
    }

}