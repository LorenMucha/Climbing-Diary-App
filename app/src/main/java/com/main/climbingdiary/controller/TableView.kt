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

    private val tableScrollView: ScrollView = view.findViewById(R.id.TableScrollView)

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
        val taskRepository = TaskRepository
        val cursor = taskRepository.getTableValues()
        //tree et because this sort the values
        val stk = view.findViewById<TableLayout>(R.id.route_table)
        //clear view
        stk.removeAllViews()
        val tbrow0 = TableRow(context)
        val tv0 = TextView(context)
        tv0.text = "Grad"
        tv0.setTextAppearance(view.context, R.style.TableHeader)
        tbrow0.addView(tv0)
        for (element in styles) {
            val tvStyle = TextView(context)
            tvStyle.text = element
            tvStyle.setPadding(20, 10, 20, 10)
            tvStyle.setTextAppearance(view.context, R.style.TableHeader)
            tbrow0.addView(tvStyle)
        }
        val tv3 = TextView(context)
        tv3.text = "GESAMT"
        tv3.setTextAppearance(view.context, R.style.TableHeader)
        tbrow0.addView(tv3)
        stk.addView(tbrow0)
        //variables
        while (!cursor.isAfterLast) {
            //get the values
            val level = cursor.getString(0)
            val os = cursor.getString(1)
            val rp = cursor.getString(2)
            val flash = cursor.getString(3)
            val gesamt = cursor.getString(4)
            //append the text view rows
            val tbrow = TableRow(context)
            val t1v = TextView(context)
            t1v.text = level
            t1v.setBackgroundColor(getGradeColor(level))
            t1v.setTextAppearance(view.context, R.style.TableRow)
            t1v.setPadding(30, 10, 20, 10)
            tbrow.addView(t1v)
            val t2v = TextView(context)
            t2v.text = os
            t2v.setBackgroundColor(getGradeColor(level))
            t2v.setTextAppearance(view.context, R.style.TableRow)
            t2v.setPadding(30, 10, 20, 10)
            tbrow.addView(t2v)
            val t3v = TextView(context)
            t3v.text = rp
            t3v.setBackgroundColor(getGradeColor(level))
            t3v.setTextAppearance(view.context, R.style.TableRow)
            t3v.setPadding(30, 10, 20, 10)
            tbrow.addView(t3v)
            val t4v = TextView(context)
            t4v.text = flash
            t4v.setBackgroundColor(getGradeColor(level))
            t4v.setTextAppearance(view.context, R.style.TableRow)
            t4v.setPadding(30, 10, 20, 10)
            tbrow.addView(t4v)
            val t5v = TextView(context)
            t5v.text = gesamt
            t5v.setBackgroundColor(getGradeColor(level))
            t5v.setTextAppearance(view.context, R.style.TableRow)
            t5v.setPadding(30, 10, 20, 10)
            tbrow.addView(t5v)
            stk.addView(tbrow)
            cursor.moveToNext()
        }
    }
}