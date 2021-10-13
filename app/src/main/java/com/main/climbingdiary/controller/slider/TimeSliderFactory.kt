package com.main.climbingdiary.controller.slider

import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.models.MenuValues
import com.main.climbingdiary.models.TimeRange

object TimeSliderFactory {

    private val sliderMap = mapOf(
        TimeRange.YEAR to TimeSlider(),
        TimeRange.RANGE to TimeRangeSlider()
    )

    fun setSlider(){
        hideAllSlider()
        val setting = AppPreferenceManager.getTimeSliderView()
        val slider =  sliderMap[setting]
        slider!!.setTimesRange()
        slider.show()
    }

    fun reloadSlider(){
        AppPreferenceManager.setFilterSetter(MenuValues.SORT_DATE)
        AppPreferenceManager.setFilter("")
        FragmentPager.refreshSelectedFragment()
        setSlider()
    }

    private fun hideAllSlider(){
        sliderMap.forEach{
                (_, v) -> v.hide()
        }
    }
}