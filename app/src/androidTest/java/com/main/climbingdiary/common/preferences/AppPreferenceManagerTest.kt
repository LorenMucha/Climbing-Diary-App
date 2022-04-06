package com.main.climbingdiary.common.preferences

import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.models.TimeRange
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppPreferenceManagerTest {

    lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = TestHelper.initDefaultScenario()
    }

    @Test
    fun shouldReturnCorrectTimeRange() {
        assertEquals(TimeRange.YEAR, AppPreferenceManager.getTimeSliderView())
        AppPreferenceManager.setTimeSliderView(TimeRange.RANGE)
        assertEquals(TimeRange.RANGE, AppPreferenceManager.getTimeSliderView())
        AppPreferenceManager.setTimeSliderView(TimeRange.YEAR)
        assertEquals(TimeRange.YEAR, AppPreferenceManager.getTimeSliderView())
    }
}