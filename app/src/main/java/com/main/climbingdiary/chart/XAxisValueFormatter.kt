package com.main.climbingdiary.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class XAxisValueFormatter(private val values: ArrayList<String>) : ValueFormatter() {
    
    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase): String {
        if (value.toInt() >= 0 && value.toInt() <= values.size - 1) {
            return values[value.toInt()]
        } else {
            return ("").toString()
        }
    }
}