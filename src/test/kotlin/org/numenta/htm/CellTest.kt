package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell(true, true, true)
        assertEquals(true, cell.isNowPredictive)
        assertEquals(true, cell.isNowActive)
        assertEquals(true, cell.isNowLearn)

        assertEquals(false, cell.isPredictive(Time.PAST))
        assertEquals(false, cell.isActive(Time.PAST))
        assertEquals(false, cell.isLearn(Time.PAST))

        cell.apply {
            isNowPredictive = false
            isNowActive = false
            isNowLearn = false
        }

        assertEquals(false, cell.isNowPredictive)
        assertEquals(false, cell.isNowActive)
        assertEquals(false, cell.isNowLearn)

        assertEquals(true, cell.isPredictive(Time.PAST))
        assertEquals(true, cell.isActive(Time.PAST))
        assertEquals(true, cell.isLearn(Time.PAST))

    }


    @Test(expected = IllegalStateException::class)
    fun getActiveSegmentThrowIllegalStateException() {
        val cell = Cell()
        cell.getActiveSegment(Time.NOW)
    }

    @Test
    fun getActiveSegment() {
        val segmentOne = Segment(1, 0.4).apply {
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
        }
        val segmentTwo = Segment(1, 0.4).apply {
            isSequenceSegment = true
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
        }
        val cell = Cell().apply {
            segments.add(segmentOne)
            segments.add(segmentTwo)
        }
        assertEquals(segmentTwo, cell.getActiveSegment(Time.NOW))

        segmentTwo.isSequenceSegment = false

        assertEquals(segmentOne, cell.getActiveSegment(Time.NOW))
    }

    @Test
    fun getBestMatchingSegment() {

        val segmentOne = Segment(1, 0.4).apply {
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
        }
        val segmentTwo = Segment(1, 0.4).apply {
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
            synapses.add(InnerSynapse(0.5, Cell(isActive = true)))
        }
        val cell = Cell().apply {
            segments.add(segmentOne)
            segments.add(segmentTwo)
        }

        assertEquals(segmentTwo, cell.getBestMatchingSegment(Time.NOW, 1))

        assertNull(cell.getBestMatchingSegment(Time.NOW,4))

    }


}