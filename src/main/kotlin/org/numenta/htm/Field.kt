package org.numenta.htm

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Field(size: Int) {
    private val matrix: MutableList<MutableList<Column>>
    private val sideSize: Int = Math.sqrt(size.toDouble()).toInt()
    private val cache: MutableMap<Pair<Column, Int>, List<Column>> = HashMap()

    val columns: List<Column>


    init {
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


    fun calculateNeighbors(column: Column, radius: Int) : List<Column> {
        val indexOf = columns.indexOf(column)
        if (indexOf == -1) {
            throw IllegalArgumentException("Column isn't in Field")
        }
        val key = Pair(column, radius)
        val cacheResult = cache[key]
        if (cacheResult != null) {
            return cacheResult
        }
        val rowNumber = indexOf / sideSize
        val columnNumber = indexOf % sideSize
        val firstRow = if (rowNumber - radius < 0) 0 else rowNumber - radius
        val lastRow = if (rowNumber + radius >= matrix.size) matrix.size - 1 else rowNumber + radius
        var result: MutableList<Column> = ArrayList()
        for (i in firstRow..lastRow) {
            val currentRow = matrix[i]
            val firstColumn = if (columnNumber - radius < 0) 0 else columnNumber - radius
            val lastColumn = if (columnNumber + radius >= currentRow.size) currentRow.size -1 else columnNumber + radius
            for (j in firstColumn..lastColumn){
                result.add(currentRow[j])
            }
        }
        result.remove(column)
        result = Collections.unmodifiableList(result)
        cache[key] = result
        return result
    }

}