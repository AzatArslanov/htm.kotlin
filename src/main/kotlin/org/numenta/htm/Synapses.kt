package org.numenta.htm

data class Synapse (var permanence: Double, val input: Int)

data class InnerSynapse(var permanence: Double, val cell: Cell)