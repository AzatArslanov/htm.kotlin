package org.numenta.htm

import org.junit.Test

import org.junit.Assert.*

class InnerSynapseTest {

    @Test
    fun copy() {
        val innerSynapse = InnerSynapse(0.5, Cell().apply { segments.add(Segment(0, 0.0)) })
        assertEquals(1, innerSynapse.cell.segments.size)
        val copy = innerSynapse.copy()
        assertEquals(0, copy.cell.segments.size)
    }
}