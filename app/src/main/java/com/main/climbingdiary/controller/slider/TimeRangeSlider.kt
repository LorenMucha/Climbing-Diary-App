package com.main.climbingdiary.controller.slider

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.RangeSlider.OnSliderTouchListener
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilterSetter
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.ShowTimeSlider.hideButton
import com.main.climbingdiary.database.TaskRepository.getYears
import com.main.climbingdiary.models.MenuValues
import java.util.*

class TimeRangeSlider : ISlider, OnSliderTouchListener {
    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val slider: RangeSlider = activity.findViewById(R.id.timerange)
    private val minText: TextView = activity.findViewById(R.id.textMin)
    private val maxText: TextView = activity.findViewById(R.id.textMax)
    private val times: ArrayList<Int>

    init {
        slider.addOnSliderTouchListener(this)
        times = initTimes()
    }

    override fun show() {
        slider.visibility = View.VISIBLE
    }

    override fun hide() {
        slider.visibility = View.GONE
    }

    override fun initTimes(): ArrayList<Int> {
        val years = ArrayList<Int>()
        getYears(false).use { cursor ->
            while (!cursor.isAfterLast) {
                years.add(cursor.getInt(0))
                cursor.moveToNext()
            }
        }
        return years
    }

    override fun setTimesRange() {
        try {
            Log.d("Years set", TextUtils.join(",", times))
            val minYear = Collections.min(times).toFloat()
            val maxYear = Collections.max(times).toFloat()
            slider.valueFrom = minYear
            slider.valueTo = maxYear
            slider.setValues(minYear,maxYear)
            minText.text = minYear.toInt().toString()
            maxText.text = maxYear.toInt().toString()
        } catch (ex: Exception) {
            hideButton()
        }
    }


    override fun onStartTrackingTouch(slider: RangeSlider) {
        val min = slider.values.min()
        val max = slider.values.max()
        minText.text = min.toInt().toString()
        maxText.text = max.toInt().toString()
    }

    override fun onStopTrackingTouch(slider: RangeSlider) {
        val minValue = slider.values.min()
        val maxValue = slider.values.max()
        val filter: String = if (minValue != maxValue) {
            "CAST(strftime('%Y',r.date) as int)>=$minValue and CAST(strftime('%Y',r.date) as int) <=$maxValue"
        } else {
            "CAST(strftime('%Y',r.date) as int)==$maxValue"
        }
        setFilterSetter(MenuValues.SORT_DATE)
        setFilter(filter)
        minText.text = minValue.toInt().toString()
        maxText.text = maxValue.toInt().toString()
        refreshSelectedFragment()
    }
}
