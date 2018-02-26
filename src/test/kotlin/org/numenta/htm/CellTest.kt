package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell(true, true, true)
        assertEquals(true, cell.isPredictive)
        assertEquals(true, cell.isActive)
        assertEquals(true, cell.isLearn)

        assertEquals(false, cell.isPastPredictive)
        assertEquals(false, cell.isPastActive)
        assertEquals(false, cell.isPastLearn)

        cell.apply {
            isPredictive = false
            isActive = false
            isLearn = false
        }

        assertEquals(false, cell.isPredictive)
        assertEquals(false, cell.isActive)
        assertEquals(false, cell.isLearn)

        assertEquals(true, cell.isPastPredictive)
        assertEquals(true, cell.isPastActive)
        assertEquals(true, cell.isPastLearn)

    }


    @Test(expected = IllegalStateException::class)
    fun getActiveSegmentThrowIllegalStateException() {
        val cell = Cell()
        cell.getActiveSegment()
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
        assertEquals(segmentTwo, cell.getActiveSegment())

        segmentTwo.isSequenceSegment = false

        assertEquals(segmentOne, cell.getActiveSegment())
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

        assertEquals(segmentTwo, cell.getBestMatchingSegment(1))

        assertNull(cell.getBestMatchingSegment(4))

    }


}