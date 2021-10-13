package com.main.climbingdiary.controller

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilterSetter
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.ShowTimeSlider.hideButton
import com.main.climbingdiary.database.TaskRepository.getYears
import com.main.climbingdiary.models.MenuValues
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
object TimeSlider: OnRangeSeekbarChangeListener, OnRangeSeekbarFinalValueListener, OnSeekbarChangeListener, OnSeekbarFinalValueListener {
    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val rangeSeekbar: CrystalRangeSeekbar = activity.findViewById(R.id.timerange)
    private val timeSeekbar : CrystalSeekbar = activity.findViewById(R.id.timeslider)
    private val minText: TextView = activity.findViewById(R.id.textMin)
    private val maxText: TextView = activity.findViewById(R.id.textMax)
    private val times: ArrayList<Int>

    init{
        /*rangeSeekbar.setOnRangeSeekbarChangeListener(this)
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(this)*/
        timeSeekbar.setOnSeekbarChangeListener(this)
        timeSeekbar.setOnSeekbarFinalValueListener(this)
        times = initTimes()
    }

    private fun initTimes(): ArrayList<Int>{
        val years = ArrayList<Int>()
        val cursor = getYears(false)
        while (!cursor.isAfterLast) {
            years.add(cursor.getInt(0))
            cursor.moveToNext()
        }
        return years
    }

    @SuppressLint("SetTextI18n")
    fun setTimesRange() {
        try {
            Log.d("Years set", TextUtils.join(",", times))
            val minYear = Collections.min(times)
            val maxYear = Collections.max(times)
/*            rangeSeekbar.setMinValue(minYear.toFloat())
            rangeSeekbar.setMaxValue(maxYear.toFloat())
            rangeSeekbar.setMinStartValue(minYear.toFloat())
            rangeSeekbar.setMaxStartValue(maxYear.toFloat())*/
            timeSeekbar.minValue = minYear.toFloat()
            timeSeekbar.maxValue = maxYear.toFloat()
            timeSeekbar.minStartValue = minYear.toFloat()
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

    override fun valueChanged(value: Number?) {
        maxText.text = value.toString()
    }

    override fun finalValue(value: Number?) {

    }
}