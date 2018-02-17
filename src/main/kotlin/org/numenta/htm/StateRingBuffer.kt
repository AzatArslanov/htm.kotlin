package org.numenta.htm

import java.util.*


class StateRingBuffer(private val size: Int) {
    private val states = LinkedList<Boolean>()

    fun addState(state: Boolean) {
        if (states.size == size) {
            states.removeFirst()
        }
        states.addLast(state)
    }

    fun countActiveState(): Int = states.count { it }
}