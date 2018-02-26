package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class SegmentTest {

    @Test
    fun states() {
        val permanence = 0.5
        val segment = Segment(2, permanence - 0.1)

        assertFalse(segment.isSegmentActive(Time.NOW))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { isNowActive = true }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { isNowActive = true }))
        assertTrue(segment.isSegmentActive(Time.NOW))

        assertFalse(segment.isSegmentLearn)
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { isNowLearn = true }))
        segment.synapses.add(InnerSynapse(permanence, Cell().apply { isNowLearn = true }))
        assertTrue(segment.isSegmentLearn)
    }

}