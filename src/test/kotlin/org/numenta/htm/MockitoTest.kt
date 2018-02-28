package org.numenta.htm

import org.mockito.Mockito

abstract class MockitoTest {

    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}