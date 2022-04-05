package com.main.climbingdiary.controller.slider

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.ShowTimeSlider.hideButton
import com.main.climbingdiary.database.TaskRepository.getYears
import com.main.climbingdiary.models.MenuValues
import java.util.*

@SuppressLint("StaticFieldLeak")
class TimeSlider : OnSeekbarChangeListener, OnSeekbarFinalValueListener, Slider {
    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private val timeSeekbar: CrystalSeekbar = activity.findViewById(R.id.timeslider)
    private val minText: TextView = activity.findViewById(R.id.textMin)
    private val maxText: TextView = activity.findViewById(R.id.textMax)
    private val times: ArrayList<Int>

    init {
        timeSeekbar.setOnSeekbarChangeListener(this)
        timeSeekbar.setOnSeekbarFinalValueListener(this)
        times = initTimes()
    }

    override fun show() {
        timeSeekbar.visibility = View.VISIBLE
    }

    override fun hide() {
        timeSeekbar.visibility = View.GONE
    }

    override fun initTimes(): ArrayList<Int> {
        val years = ArrayList<Int>()
        val cursor = getYears(false)
        while (!cursor.isAfterLast) {
            years.add(cursor.getInt(0))
            cursor.moveToNext()
        }
        return years
    }

    override fun setTimesRange() {
        try {
            Log.d("Years set", TextUtils.join(",", times))
            val minYear = Collections.min(times).toFloat()
            val maxYear = Collections.max(times).toFloat()
            timeSeekbar.minValue = minYear
            timeSeekbar.maxValue = maxYear
            timeSeekbar.minStartValue = maxYear
            timeSeekbar.minStartValue = maxYear
            timeSeekbar.apply()
            minText.text = minYear.toInt().toString()
            maxText.text = maxYear.toInt().toString()
        } catch (ex: Exception) {
            hideButton()
        }
    }

    override fun valueChanged(value: Number?) {
        maxText.text = value.toString()
    }

    override fun finalValue(value: Number?) {
        val min = value.toString()
        val filter = "CAST(strftime('%Y',r.date) as int)==$min"
        AppPreferenceManager.setFilterSetter(MenuValues.SORT_DATE)
        AppPreferenceManager.setFilter(filter)
        refreshSelectedFragment()
    }
}