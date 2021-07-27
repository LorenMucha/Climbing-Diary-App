package com.main.climbingdiary.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

class XAxisValueFormatter(private val values: ArrayList<String>) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        // "value" represents the position of the label on the axis (x or y)
        return try {
            values[value.toInt()]
        } catch (e: Exception) {
            "False"
        }
    }
}