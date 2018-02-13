package org.numenta.htm

import java.util.*
import kotlin.collections.ArrayList

class Field(size: Int) {
    private val matrix: MutableList<MutableList<Column>>

    val columns: List<Column>


    init {
        val sideSize = Math.sqrt(size.toDouble()).toInt()
        matrix = ArrayList()
        val list = ArrayList<Column>()
        for (i in 0 until sideSize) {
            val columns = ArrayList<Column>()
            for (j in 0 until sideSize) {
                val column = Column()
                columns.add(column)
                list.add(column)
            }
            matrix.add(columns)
        }
        if (sideSize * sideSize != size) {
            val countToAdd = size - sideSize * sideSize
            val rowToAdd = ArrayList<Column>()
            for (i in 0 until countToAdd) {
                val column = Column()
                rowToAdd.add(column)
                list.add(column)
            }
            matrix.add(rowToAdd)
        }
        columns = Collections.unmodifiableList(list)
    }

}