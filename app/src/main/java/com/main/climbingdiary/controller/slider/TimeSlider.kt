package com.main.climbingdiary.controller.slider

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.ShowTimeSlider.hideButton
import com.main.climbingdiary.database.TaskRepository.getYears
import com.main.climbingdiary.models.MenuValues
import java.util.Collections

class TimeSlider : ISlider, OnSliderTouchListener {
    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val slider: Slider = activity.findViewById(R.id.time_slider)
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
            val minYear = Collections.min(times).toFloat()
            val maxYear = Collections.max(times).toFloat()
            slider.valueFrom = minYear
            slider.valueTo = maxYear
            slider.value = maxYear
            minText.text = minYear.toInt().toString()
            maxText.text = maxYear.toInt().toString()
        } catch (ex: Exception) {
            hideButton()
        }
    }

    override fun onStartTrackingTouch(slider: Slider) {
        minText.text = slider.value.toInt().toString()
    }

    override fun onStopTrackingTouch(slider: Slider) {
        minText.text = slider.value.toInt().toString()
        val min = slider.value.toString()
        val filter = "CAST(strftime('%Y',r.date) as int)==$min"
        AppPreferenceManager.setFilterSetter(MenuValues.SORT_DATE)
        AppPreferenceManager.setFilter(filter)
        refreshSelectedFragment()
    }
}
