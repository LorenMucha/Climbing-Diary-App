package com.main.climbingdiary.controller

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilterSetter
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.ShowTimeSlider.hideButton
import com.main.climbingdiary.database.TaskRepository.getYears
import com.main.climbingdiary.models.MenuValues
import java.util.*

@SuppressLint("StaticFieldLeak")
object TimeSlider: OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener {
    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val rangeSeekbar: CrystalRangeSeekbar = activity.findViewById(R.id.timerange)
    private val minText: TextView = activity.findViewById(R.id.textMin)
    private val maxText: TextView = activity.findViewById(R.id.textMax)

    init{
        rangeSeekbar.setOnRangeSeekbarChangeListener(this)
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun setTimes() {
        try {
            val years = ArrayList<Int>()
            val cursor = getYears(false)
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
            hideButton()
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
        val filter: String = if (minValue != maxValue) {
            "CAST(strftime('%Y',r.date) as int)>=$min and CAST(strftime('%Y',r.date) as int) <=$max"
        } else {
            "CAST(strftime('%Y',r.date) as int)==$max"
        }
        setFilterSetter(MenuValues.SORT_DATE)
        setFilter(filter)
        minText.text = min
        maxText.text = max
        refreshSelectedFragment()
    }
}