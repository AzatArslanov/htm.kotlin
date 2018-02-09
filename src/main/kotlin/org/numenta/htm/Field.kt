package org.numenta.htm

class Field(val size: Int) {
    private val columns: MutableList<Column>

    init {
        columns = ArrayList()
    }


}