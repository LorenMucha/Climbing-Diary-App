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
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.button.ShowTimeSlider;
import com.main.climbingdiary.database.TaskRepository;
import com.main.climbingdiary.models.MenuValues;

import java.util.ArrayList;
import java.util.Collections;

public class TimeSlider implements OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {

    private final CrystalRangeSeekbar rangeSeekbar;
    private final TextView minText;
    private final TextView maxText;
    @SuppressLint("StaticFieldLeak")
    private static volatile TimeSlider INSTANZ = null;

    public static synchronized TimeSlider getInstance(){
        if(INSTANZ== null){
            INSTANZ = new TimeSlider();
        }
        return INSTANZ;
    }

    private TimeSlider(){
        Activity _activity = MainActivity.getMainActivity();
        rangeSeekbar = _activity.findViewById(R.id.timerange);
        minText = _activity.findViewById(R.id.textMin);
        maxText = _activity.findViewById(R.id.textMax);
        rangeSeekbar.setOnRangeSeekbarChangeListener(this);
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void setTimes(){
        try {
            ArrayList<Integer> years = new ArrayList<>();
            Cursor cursor = TaskRepository.INSTANCE.getYears(false);
            if (cursor != null) {
                while (!cursor.isAfterLast()) {
                    years.add(cursor.getInt(0));
                    cursor.moveToNext();
                }
            }
            Log.d("Years set", TextUtils.join(",", years));
            int minYear = Collections.min(years);
            int maxYear = Collections.max(years);
            rangeSeekbar.setMinValue(minYear);
            rangeSeekbar.setMaxValue(maxYear);
            rangeSeekbar.setMinStartValue(minYear);
            rangeSeekbar.setMaxStartValue(maxYear);
            minText.setText(Integer.toString(minYear));
            maxText.setText(Integer.toString(maxYear));
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
        final String min = String.valueOf(minValue);
        final String max = String.valueOf(maxValue);
        final String filter;
        if(!minValue.equals(maxValue)){
            filter = "CAST(strftime('%Y',r.date) as int)>=" + min + " and CAST(strftime('%Y',r.date) as int) <=" + max;
        }else{
            filter = "CAST(strftime('%Y',r.date) as int)==" + max;
        }
        AppPreferenceManager.setFilterSetter(MenuValues.SORT_DATE);
        AppPreferenceManager.setFilter(filter);
        minText.setText(min);
        maxText.setText(max);
        FragmentPager.getInstance().refreshSelectedFragment();
    }
}