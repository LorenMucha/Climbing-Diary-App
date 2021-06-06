package com.main.climbingdiary.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.button.ShowTimeSlider
import com.main.climbingdiary.database.TaskRepository
import com.main.climbingdiary.model.MenuValues
import java.util.*

@SuppressLint("StaticFieldLeak")
object TimeSlider : OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {

    private val rangeSeekbar: CrystalRangeSeekbar
    private val minText: TextView
    private val maxText: TextView

    init{
        val activity:Activity = MainActivity.getApplication() as Activity
        rangeSeekbar = activity.findViewById(R.id.timerange)
        minText = activity.findViewById(R.id.textMin)
        maxText = activity.findViewById(R.id.textMax)
        rangeSeekbar.setOnRangeSeekbarChangeListener(this@TimeSlider)
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this@TimeSlider)
    }

    fun setTimes() {
        try {
            val years = ArrayList<Int>()
            val cursor: Cursor = TaskRepository.getYears(false)
            while (!cursor.isAfterLast) {
                years.add(cursor.getInt(0))
                cursor.moveToNext()
            }
            Log.d("Years set", TextUtils.join(",", years))
            val minYear = Collections.min(years)
            val maxYear = Collections.max(years)
            rangeSeekbar.setMinValue(minYear.toFloat())
            rangeSeekbar.setMaxValue(maxYear.toFloat())
            rangeSeekbar.setMinStartValue(minYear.toFloat())
            rangeSeekbar.setMaxStartValue(maxYear.toFloat())
            minText.text = minYear.toString()
            maxText.text = maxYear.toString()
        } catch (ex: Exception) {
            ShowTimeSlider.hide()
        }
    }

    override fun valueChanged(minValue: Number, maxValue: Number) {
        val min = minValue.toString()
        val max = maxValue.toString()
        minText.text = min
        maxText.text = max
    }

    override fun finalValue(minValue: Number, maxValue: Number) {
        val min = minValue.toString()
        val max = maxValue.toString()
        val filter: String
        filter = if (minValue != maxValue) {
            "CAST(strftime('%Y',r.date) as int)>=$min and CAST(strftime('%Y',r.date) as int) <=$max"
        } else {
            "CAST(strftime('%Y',r.date) as int)==$max"
        }
        AppPreferenceManager.setFilterSetter(MenuValues.SORT_DATE)
        AppPreferenceManager.setFilter(filter)
        minText.text = min
        maxText.text = max
        FragmentPager.refreshSelectedFragment()
    }
}