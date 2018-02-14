package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class FieldTest {

    @Test
    fun initialization() {
        val size = 0
        val field = Field(size)
        assertTrue(size == field.columns.size)
    }

    @Test
    fun calculateNeighbors() {
        var size = 25
        var index = 0
        var radius = 1
        var field = Field(size)
        var neighbors = field.calculateNeighbors(field.columns[index], radius)
        assertTrue(neighbors.size == 3)

        index = 24
        neighbors = field.calculateNeighbors(field.columns[index], radius)
        assertTrue(neighbors.size == 3)

        size = 11
        index = 10
        radius = 2
        field = Field(size)
        neighbors = field.calculateNeighbors(field.columns[index], radius)
        assertTrue(neighbors.size == 7)
    }
}