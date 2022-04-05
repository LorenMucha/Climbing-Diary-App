package com.main.climbingdiary.models

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class TimeRangeTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private val ressourceArray: Array<String> by lazy {
        MainActivity.getMainActivity()
            .resources.getStringArray(R.array.entries_time_slider)
    }

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = launch(MainActivity::class.java)
    }

    @Test
    fun shouldtranslateValidTimeRange() {
        assertEquals(TimeRange.YEAR, TimeRange.translate(ressourceArray[0]))
        assertEquals(TimeRange.RANGE, TimeRange.translate(ressourceArray[1]))
    }

    @Test
    fun shouldtranslateValidString() {
        assertEquals(ressourceArray[0], TimeRange.translate(TimeRange.YEAR))
        assertEquals(ressourceArray[1], TimeRange.translate(TimeRange.RANGE))
    }
}