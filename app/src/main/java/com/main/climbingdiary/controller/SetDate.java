package com.main.climbingdiary.controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetDate implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private final EditText editText;
    private final Calendar calender;
    private final Context context;

    public SetDate(EditText editText, Context ctx){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        calender = Calendar.getInstance();
        this.context = ctx;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
        //bring String in SQL-Format
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.GERMAN);
        calender.set(Calendar.YEAR, year);
        calender.set(Calendar.MONTH, monthOfYear);
        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        editText.setText(sdformat.format(calender.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            new DatePickerDialog(this.context, this, calender
                    .get(Calendar.YEAR), calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

}
