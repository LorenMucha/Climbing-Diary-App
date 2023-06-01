package com.main.climbingdiary.chart

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertFactory.getErrorAlert
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.Colors

class RouteBarChartController(val view: View) : RouteChartController() {

    private val barChart: BarChart = view.findViewById(R.id.route_bar_chart)
    private val context: Context = view.context

    override fun show() {
        barChart.visibility = View.VISIBLE
    }

    override fun hide() {
        barChart.visibility = View.GONE
    }

    override fun createChart() {
        try {
            val entriesGroup: MutableList<BarEntry> = ArrayList()
            val labels = ArrayList<String>()
            //get the cjart entries
            //String Sort = (Menu) getA
            val cursor = TaskRepository.getBarChartValues()
            var i = 0
            while (!cursor.isAfterLast) {
                val level = cursor.getString(0)
                val sumRp = cursor.getFloat(1)
                val sumOs = cursor.getFloat(2)
                val sumFlash = cursor.getFloat(3)
                labels.add(level)
                entriesGroup.add(BarEntry(i.toFloat(), floatArrayOf(sumFlash, sumOs, sumRp)))
                cursor.moveToNext()
                i++
            }
            val set = BarDataSet(entriesGroup, "")
            set.setColors(
                Colors.getStyleColor("flash"),
                Colors.getStyleColor("rp"),
                Colors.getStyleColor("os")
            )
            set.setDrawValues(false)
            set.stackLabels = arrayOf("FLASH", "OS", "RP")

            val data = BarData(set)
            barChart.data = data
            barChart.description.isEnabled = false
            barChart.setFitBars(true) // make the x-axis fit exactly all bars
            barChart.axisRight.isEnabled = false
            //set thex axis
            val xAxis = barChart.xAxis
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = labels.size
            xAxis.valueFormatter = XAxisValueFormatter(labels)
            val yAxis = barChart.axisLeft
            yAxis.spaceBottom = 1f
            barChart.invalidate() // refresh

            //set the on click Listener
            barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry, h: Highlight) {
                    val x = e.x
                    e.y.toInt()
                    val style = set.stackLabels[h.stackIndex]
                    val sum = entriesGroup[x.toInt()].yVals[h.stackIndex]
                    Toast.makeText(
                        context,
                        String.format("Stil: %s\nAnzahl: %s", style, sum),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected() {}
            })
        } catch (ex: Exception) {
            getErrorAlert(context).show()
            Log.d("Erstellung Barchart:", ex.localizedMessage)
        }
    }

}