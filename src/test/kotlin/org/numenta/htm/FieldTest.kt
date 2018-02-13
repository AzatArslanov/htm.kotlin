package org.numenta.htm

import org.junit.Assert.*
import org.junit.Test

class FieldTest {

    @Test
    fun initialization() {
        val size = 17
        val field = Field(size)
        assertTrue(size == field.columns.size)
    }
}