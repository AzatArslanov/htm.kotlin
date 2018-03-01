package org.numenta.htm


class TemporalPooling(val field: Field) {

    fun getLearnCells(time: Time):List<Cell> {
        val result = ArrayList<Cell>()
        field.columns.forEach {
            result.addAll(it.cells.filter { it.isLearn(time) })
        }
        return result
    }
}

