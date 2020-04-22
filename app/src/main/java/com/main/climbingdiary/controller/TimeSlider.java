package com.main.climbingdiary.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.controller.button.ShowTimeSlider;
import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.Filter;

import java.util.ArrayList;
import java.util.Collections;

public class TimeSlider implements OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {
    private static CrystalRangeSeekbar rangeSeekbar;
    @SuppressLint("StaticFieldLeak")
    private static TextView minText;
    @SuppressLint("StaticFieldLeak")
    private static TextView maxText;
    private final static TaskRepository taskRepository = TaskRepository.getInstance();

    static {
        Activity _activity = MainActivity.getMainActivity();
        rangeSeekbar = _activity.findViewById(R.id.timerange);
        minText = _activity.findViewById(R.id.textMin);
        maxText = _activity.findViewById(R.id.textMax);
    }

    public TimeSlider(){
        setTimes();
        rangeSeekbar.setOnRangeSeekbarChangeListener(this);
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this);
    }

    @SuppressLint("SetTextI18n")
    private static void setTimes(){
        try {
            ArrayList<Integer> years = new ArrayList<>();
            Cursor cursor = taskRepository.getYears();
            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    years.add(cursor.getInt(0));
                    cursor.moveToNext();
                }
            }
            Log.d("Years set", TextUtils.join(",", years));
            int minYear = Collections.min(years);
            int maxYear = Collections.max(years);
            Log.d("MIN || MAX", minYear + "||" + maxYear);
            rangeSeekbar.setMinValue(minYear);
            rangeSeekbar.setMaxValue(maxYear);
            rangeSeekbar.setMinStartValue(minYear);
            rangeSeekbar.setMaxStartValue(maxYear);
            minText.setText(Integer.toString(minYear));
            maxText.setText(Integer.toString(maxYear));
            ShowTimeSlider.showButton();
        }catch(Exception ex){
            ShowTimeSlider.hideButton();
        }
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
            String filter = "CAST(strftime('%Y',r.date) as int)>="+min+" and CAST(strftime('%Y',r.date) as int) <="+max;
            Filter.setFilter(filter, MenuValues.SORT_DATE);
        }else{
            String filter = "CAST(strftime('%Y',r.date) as int)=="+max;
            Filter.setFilter(filter,MenuValues.SORT_DATE);
        }
        Log.d("Filter set",Filter.getFilter());
        minText.setText(min);
        maxText.setText(max);
        FragmentPager.refreshAllFragments();
    }
}