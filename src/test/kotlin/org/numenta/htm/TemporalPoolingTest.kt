package org.numenta.htm

import org.junit.Test

import org.junit.Assert.*

class TemporalPoolingTest {

    @Test
    fun saveFieldState() {
        val field = Field(2, 1)
        val temporalPooling = TemporalPooling(field)
        val permanence = 0.5
        val activationThreshold = 1
        val connectedPerm = 0.5
        val firstCell = field.columns[0].cells[0]
        val secondCell = field.columns[1].cells[0]
        firstCell.segments.add(Segment(activationThreshold, connectedPerm).apply {
            synapses.add(InnerSynapse(permanence, secondCell))
        })

        field.columns.forEach {
            it.cells.forEach {
                assertNull(it.pastState)
            }
        }

        temporalPooling.saveFieldState()

        field.columns.forEach {
            it.cells.forEach {
                assertNotNull(it.pastState)
            }
        }

        assertEquals(firstCell.pastState?.segments?.get(0)?.synapses?.get(0)?.cell, secondCell.pastState)
        assertEquals(firstCell.segments[0].activationThreshold, firstCell.pastState?.segments?.get(0)?.activationThreshold)
        assertEquals(firstCell.segments[0].synapses[0].permanence, firstCell.pastState?.segments?.get(0)?.synapses?.get(0)?.permanence)
    }
}