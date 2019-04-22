package com.example.climbingdiary.Ui.charts;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class XAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<String> mValues;

    public XAxisValueFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        try {
            return mValues.get((int) value);
        }catch(Exception e){
            return "False";
        }
    }
}