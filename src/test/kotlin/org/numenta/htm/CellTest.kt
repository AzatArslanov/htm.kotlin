package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class CellTest {

    @Test
    fun predictiveState() {
        val cell = Cell(true, true, true)
        assertEquals(true, cell.isPredictive(Time.NOW))
        assertEquals(true, cell.isActive(Time.NOW))
        assertEquals(true, cell.isLearn(Time.NOW))

        assertEquals(false, cell.isPredictive(Time.PAST))
        assertEquals(false, cell.isActive(Time.PAST))
        assertEquals(false, cell.isLearn(Time.PAST))

        cell.fixStates()


        assertEquals(false, cell.isPredictive(Time.NOW))
        assertEquals(false, cell.isActive(Time.NOW))
        assertEquals(false, cell.isLearn(Time.NOW))

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

    @Test
    fun adaptSegments() {
        val innerSynapseOne = InnerSynapse(0.4, Cell())
        val innerSynapseTwo = InnerSynapse(0.2, Cell())
        val segmentOne = Segment(0, 0.0).apply {
            synapses.add(innerSynapseOne)
            isSequenceSegment = false
        }
        val update = Cell.Update(segmentOne).apply {
            isSequenceSegment = true
            adaptSynapses.add(innerSynapseOne)
            newSynapses.add(innerSynapseTwo)
        }
        val cell = Cell().apply {
            segments.add(segmentOne)
            toUpdate.add(update)
        }
        cell.adaptSegments(0.1, 0.2, true)
        assertEquals(0, cell.toUpdate.size)
        assertEquals(1, cell.segments.size)
        assertEquals(true, segmentOne.isSequenceSegment)
        assertEquals(2, segmentOne.synapses.size)
        assertEquals(0.5, innerSynapseOne.permanence, 0.0)
        assertEquals(0.2, innerSynapseTwo.permanence, 0.0)

        cell.toUpdate.add(update)
        cell.adaptSegments(0.1, 0.5, false)

        assertEquals(0.0, innerSynapseOne.permanence, 0.0)
        assertEquals(0.2, innerSynapseTwo.permanence, 0.0)
    }


}