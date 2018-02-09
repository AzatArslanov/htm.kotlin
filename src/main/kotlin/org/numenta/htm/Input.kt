package org.numenta.htm

import java.util.*

data class Input(private val input: IntArray) {

    fun contains(element: Int) = input.contains(element)



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Input

        if (!Arrays.equals(input, other.input)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(input)
    }
}