package com.main.climbingdiary.controller

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class SetDate (private val editText: EditText, val context: Context): View.OnFocusChangeListener, OnDateSetListener {
    private val calender: Calendar = Calendar.getInstance()
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        //bring String in SQL-Format
        val myFormat = "yyyy-MM-dd"
        val sdformat = SimpleDateFormat(myFormat, Locale.GERMAN)
        calender[Calendar.YEAR] = year
        calender[Calendar.MONTH] = monthOfYear
        calender[Calendar.DAY_OF_MONTH] = dayOfMonth
        editText.setText(sdformat.format(calender.time))
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            DatePickerDialog(
                context, this, calender[Calendar.YEAR],
                calender[Calendar.MONTH],
                calender[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }
}