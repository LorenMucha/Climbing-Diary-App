package com.main.climbingdiary.Ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.StatisticFragment;
import com.main.climbingdiary.abstraction.RouteFragment;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Filter;

import java.util.ArrayList;
import java.util.Collections;

public class TimeSlider implements OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {
    private static CrystalRangeSeekbar rangeSeekbar;
    private static TextView minText;
    private static TextView maxText;
    private final static TaskRepository taskRepository = new TaskRepository();

    public TimeSlider(Activity _activity){
        rangeSeekbar = _activity.findViewById(R.id.timerange);
        minText = _activity.findViewById(R.id.textMin);
        maxText = _activity.findViewById(R.id.textMax);
        setTimes();
        rangeSeekbar.setOnRangeSeekbarChangeListener(this);
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this);
    }

    @SuppressLint("SetTextI18n")
    public static void setTimes(){
        ArrayList<Integer> years = new ArrayList<>();
        taskRepository.open();
        Cursor cursor = taskRepository.getYears();
        if (cursor != null) {
            while (!cursor.isAfterLast()) {
                years.add(cursor.getInt(0));
                cursor.moveToNext();
            }
        }
        taskRepository.close();
        Log.d("Years set", TextUtils.join(",",years));
        int minYear = Collections.min(years);
        int maxYear = Collections.max(years);
        Log.d("MIN || MAX",minYear+"||"+maxYear);
        rangeSeekbar.setMinValue(minYear);
        rangeSeekbar.setMaxValue(maxYear);
        rangeSeekbar.setMinStartValue(minYear);
        rangeSeekbar.setMaxStartValue(maxYear);
        minText.setText(Integer.toString(minYear));
        maxText.setText(Integer.toString(maxYear));
    }

    @Override
    public void valueChanged(Number minValue, Number maxValue) {
        String min = String.valueOf(minValue);
        String max = String.valueOf(maxValue);
        minText.setText(min);
        maxText.setText(max);
    }

    @Override
    public void finalValue(Number minValue, Number maxValue) {
        String min = String.valueOf(minValue);
        String max = String.valueOf(maxValue);
        if(!minValue.equals(maxValue)){
            String filter = "CAST(strftime('%Y',r.date) as int)<="+min+" or CAST(strftime('%Y',r.date) as int) >="+max;
            Filter.setFilter(filter);
        }else{
            String filter = "CAST(strftime('%Y',r.date) as int)=="+max;
            Filter.setFilter(filter);
        }
        minText.setText(min);
        maxText.setText(max);
        RouteFragment.refreshData();
        StatisticFragment.refreshData();
    }

    public static int getMin(){
        return Integer.parseInt(minText.getText().toString());
    }
    public static int getMax(){
        return Integer.parseInt(maxText.getText().toString());
    }
}
