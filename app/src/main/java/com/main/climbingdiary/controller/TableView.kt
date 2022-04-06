package com.main.climbingdiary.controller

import android.annotation.SuppressLint
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
    private val tableCursor by lazy { TaskRepository.getTableValues() }

    fun show() {
        this.tableScrollView.visibility = View.VISIBLE
    }

    fun hide() {
        this.tableScrollView.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun createTableView() {
        //Variables
        val styles = getStyle(true)
        //tree et because this sort the values
        val stk = view.findViewById<TableLayout>(R.id.route_table)
        //clear view
        stk.removeAllViews()
        val tbrow0 = TableRow(context)
        val tv0 = TextView(context)
        with(tv0) {
            this.text = context.getString(R.string.table_level_header)
            text = context.getString(R.string.table_level_header)
            setTextAppearance(view.context, R.style.TableHeader)
        }
        tbrow0.addView(tv0)
        for (element in styles) {
            val tvStyle = TextView(context)
            tvStyle.text = element
            tvStyle.setPadding(20, 10, 20, 10)
            tvStyle.setTextAppearance(view.context, R.style.TableHeader)
            tbrow0.addView(tvStyle)
        }
        val tv3 = TextView(context)
        with(tv3) {
            this.text = context.getString(R.string.table_gesamt_header)
            setTextAppearance(view.context, R.style.TableHeader)
        }
        tbrow0.addView(tv3)
        stk.addView(tbrow0)
        //variables
        while (!tableCursor.isAfterLast) {
            //get the values
            val level = tableCursor.getString(0)
            val os = tableCursor.getString(1)
            val rp = tableCursor.getString(2)
            val flash = tableCursor.getString(3)
            val gesamt = tableCursor.getString(4)
            //append the text view rows
            val tbrow = TableRow(context)
            val t1v = TextView(context)
            with(t1v) {
                this.text = level
                setBackgroundColor(getGradeColor(level))
                setTextAppearance(view.context, R.style.TableRow)
                setPadding(30, 10, 20, 10)
            }
            tbrow.addView(t1v)
            val t2v = TextView(context)
            with(t2v) {
                this.text = os
                setBackgroundColor(getGradeColor(level))
                setTextAppearance(view.context, R.style.TableRow)
                setPadding(30, 10, 20, 10)
            }
            tbrow.addView(t2v)
            val t3v = TextView(context)
            with(t3v) {
                this.text = rp
                setBackgroundColor(getGradeColor(level))
                setTextAppearance(view.context, R.style.TableRow)
                setPadding(30, 10, 20, 10)
            }
            tbrow.addView(t3v)
            val t4v = TextView(context)
            with(t4v) {
                this.text = flash
                setBackgroundColor(getGradeColor(level))
                setTextAppearance(view.context, R.style.TableRow)
                setPadding(30, 10, 20, 10)
            }
            tbrow.addView(t4v)
            val t5v = TextView(context)
            with(t5v) {
                this.text = gesamt
                setBackgroundColor(getGradeColor(level))
                setTextAppearance(view.context, R.style.TableRow)
                setPadding(30, 10, 20, 10)
            }
            tbrow.addView(t5v)
            stk.addView(tbrow)
            tableCursor.moveToNext()
        }
    }

    private fun createLevelColumn(){

    }
}