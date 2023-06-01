package com.main.climbingdiary.chart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AlertFactory.getErrorAlert
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.Colors
import com.main.climbingdiary.models.Levels
import com.main.climbingdiary.models.Styles
import java.util.*

class RouteLineChartController(val view:View) : RouteChartController() {

    private val lineChart: LineChart = view.findViewById(R.id.route_line_chart)
    private val context: Context = view.context
    private val routeResults = LinkedHashMap<Int, List<InfoObject>>()

    override fun show() {
        lineChart.visibility = View.VISIBLE
    }

    override fun hide() {
        lineChart.visibility = View.GONE
    }

    override fun createChart() {
        val labels = ArrayList<String>()
        val entries = ArrayList<Entry>()
        val yearCursor = TaskRepository.getYears(true)
        //transform the cursor to an array list
        val yearList = ArrayList<Int>()
        routeResults.clear()
        try {
            var i = 0
            while (!yearCursor.isAfterLast) {
                yearList.add(yearCursor.getInt(0))
                yearCursor.moveToNext()
            }
            yearList.sort()
            for (yearVal in yearList) {
                val cursor = TaskRepository.getTopTenRoutes(yearVal)
                val routesYear: MutableList<InfoObject> = ArrayList()
                var sum = 0
                while (!cursor.isAfterLast) {
                    val level = cursor.getString(0)
                    val stil = cursor.getString(1)
                    val points: Int =
                        Levels.getLevelRating(level) + Styles.getStyleRatingFactor(stil)
                    val route = InfoObject()
                    route.points = points
                    route.route_level = level
                    route.routeStil = stil
                    route.routeName = cursor.getString(2)
                    routesYear.add(route)
                    sum += points
                    cursor.moveToNext()
                }
                labels.add(yearVal.toString())
                routeResults[yearVal] = routesYear
                entries.add(Entry(i.toFloat(), sum.toFloat()))
                i++
            }
            labels.sort()
            val dataSet = LineDataSet(entries, "")
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            //to enable the cubic density : if 1 then it will be sharp curve
            //to fill the below of smooth line in graph
            dataSet.setDrawFilled(true)
            dataSet.fillColor = Color.BLACK
            //set the transparency
            dataSet.fillAlpha = 80
            dataSet.setDrawValues(false)
            //set the gradiant then the above draw fill color will be replace
            val drawable: Drawable? = ContextCompat.getDrawable(
                MainActivity.getMainAppContext(),
                R.drawable.radiant_linechart
            )
            dataSet.fillDrawable = drawable
            // Setting Data
            val data = LineData(dataSet)
            lineChart.data = data
            lineChart.description.isEnabled = false
            lineChart.legend.isEnabled = false
            //set thex axis
            val xAxis = lineChart.xAxis
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = XAxisValueFormatter(labels)
            val yAxis = lineChart.axisLeft
            yAxis.setDrawGridLines(false)
            yAxis.spaceBottom = 5f
            yAxis.spaceTop = 5f
            yAxis.axisMaximum = data.yMax + 200
            //refresh
            lineChart.invalidate()
            lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry, h: Highlight) {
                    val year = yearList[e.x.toInt()]
                    showDialog(Objects.requireNonNull(routeResults[year])!!, year)
                }

                override fun onNothingSelected() {}
            })
        } catch (ex: Exception) {
            getErrorAlert(context).show()
            Log.d("Erstellung Line chart:", ex.localizedMessage)
        }
    }

    private fun showDialog(objectList: List<InfoObject>, year: Int) {
        val builder = AlertDialog.Builder(context)
        val dialogContext = builder.context
        val inflater = LayoutInflater.from(dialogContext)
        val points: Int = objectList.sumOf { it.points!! }
        @SuppressLint("InflateParams") val alertView: View =
            inflater.inflate(R.layout.dialog_line_chart_top_ten, null)
        builder.setView(alertView)
        builder.setTitle("${StringManager.getStringForId(R.string.line_chart_dialog_hardest_routes)} - $year \nPunkte: $points")
        val tableLayout = alertView.findViewById<TableLayout>(R.id.tableLayoutTopTen)
        for (infoObject in objectList) {
            val tableRow = TableRow(dialogContext)
            val params = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.layoutParams = params
            val textViewName = TextView(dialogContext)
            textViewName.text = infoObject.routeName
            textViewName.setTypeface(null, Typeface.BOLD)
            textViewName.setTextColor(context.resources.getColor(R.color.black))
            textViewName.setPadding(20, 0, 0, 0)
            tableRow.addView(textViewName)
            val textViewLevel = TextView(dialogContext)
            textViewLevel.text = infoObject.route_level
            textViewLevel.setTypeface(null, Typeface.BOLD)
            textViewLevel.setTextColor(Colors.getGradeColor(infoObject.route_level!!))
            textViewLevel.setPadding(20, 0, 0, 0)
            tableRow.addView(textViewLevel)
            val textViewStil = TextView(dialogContext)
            textViewStil.text = infoObject.routeStil
            textViewStil.setTypeface(null, Typeface.BOLD)
            textViewStil.setTextColor(Colors.getStyleColor(infoObject.routeStil!!))
            textViewStil.setPadding(20, 0, 0, 0)
            tableRow.addView(textViewStil)
            tableLayout.addView(tableRow)
        }
        builder.setCancelable(true)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    data class InfoObject(
        var route_level: String? = null,
        var routeName: String? = null,
        var routeStil: String? = null,
        var points: Int? = 0
    )

}