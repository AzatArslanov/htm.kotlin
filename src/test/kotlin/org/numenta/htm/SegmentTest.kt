package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class SegmentTest {

    @Test
    fun copy() {
        val cell = Cell()
        val innerSynapse = InnerSynapse(0.0, cell)
        val segment = Segment().apply { 
            synapses.add(innerSynapse)
        }

        val twin = segment.copy()

        assertNotEquals(System.identityHashCode(segment), System.identityHashCode(twin))
        assertEquals(1, twin.synapses.size)
        assertNotEquals(System.identityHashCode(innerSynapse), System.identityHashCode(twin.synapses[0]))
        assertEquals(System.identityHashCode(cell), System.identityHashCode(twin.synapses[0].cell))
    }

}