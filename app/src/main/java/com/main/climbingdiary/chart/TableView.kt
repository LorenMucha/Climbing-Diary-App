package com.main.climbingdiary.chart

import android.content.Context
import android.view.View
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.main.climbingdiary.R
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.models.Colors.getGradeColor
import com.main.climbingdiary.models.Styles.getStyle

class TableView(val context: Context, val view: View) {

    private val tableScrollView: ScrollView = view.findViewById(R.id.table_scroll_view)

    fun show() {
        this.tableScrollView.visibility = View.VISIBLE
    }

    fun hide() {
        this.tableScrollView.visibility = View.GONE
    }

    fun createTableView() {
        //Variables )
        //tree et because this sort the values
        val tableView = view.findViewById<TableLayout>(R.id.route_table)
        val tableCursor = TaskRepository.getTableValues()
        tableView.removeAllViews()

        this.createTableHeader(tableView)
        //variables
        while (!tableCursor.isAfterLast) {
            //get the values
            val level = tableCursor.getString(0)
            val os = tableCursor.getString(1)
            val rp = tableCursor.getString(2)
            val flash = tableCursor.getString(3)
            val gesamt = tableCursor.getString(4)
            val bodyRows = TableRow(context)
            //append the text view rows
            val t1v = appendTableRow(level, level)
            bodyRows.addView(t1v)
            val t2v = appendTableRow(os, level)
            bodyRows.addView(t2v)
            val t3v = appendTableRow(rp, level)
            bodyRows.addView(t3v)
            val t4v = appendTableRow(flash, level)
            bodyRows.addView(t4v)
            val t5v = appendTableRow(gesamt, level)
            bodyRows.addView(t5v)
            tableView.addView(bodyRows)
            tableCursor.moveToNext()
        }
    }

    private fun appendTableRow(text: String, level: String): TextView {
        val textView = TextView(context)
        with(textView) {
            this.text = text
            setBackgroundColor(getGradeColor(level))
            setTextAppearance(R.style.TableRow)
            setPadding(30, 10, 20, 10)
        }
        return textView
    }

    private fun createTableHeader(tableView: TableLayout) {
            val tableRow = TableRow(context)
            val styles = getStyle(true)
            val tv0 = TextView(context)
            with(tv0) {
                this.text = context.getString(R.string.table_level_header)
                text = context.getString(R.string.table_level_header)
                setTextAppearance(R.style.TableHeader)
            }
            tableRow.addView(tv0)
            for (element in styles) {
                val tvStyle = TextView(context)
                tvStyle.text = element
                tvStyle.setPadding(20, 10, 20, 10)
                tvStyle.setTextAppearance(R.style.TableHeader)
                tableRow.addView(tvStyle)
            }
            val tv3 = TextView(context)
            with(tv3) {
                this.text = context.getString(R.string.table_gesamt_header)
                setTextAppearance(R.style.TableHeader)
            }
            tableRow.addView(tv3)
            tableView.addView(tableRow)
    }
}