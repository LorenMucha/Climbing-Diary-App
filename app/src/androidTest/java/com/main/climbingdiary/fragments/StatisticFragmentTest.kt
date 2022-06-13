package com.main.climbingdiary.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class StatisticFragmentTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = initDefaultScenario()
        onView(withId(R.id.btn_stat)).perform(click())
    }

    @Test
    fun shouldCreateStatisticViewOnButtonClick() {
        onView(withId(R.id.route_bar_chart)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_dev)).perform(click())
        onView(withId(R.id.route_line_chart)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_table)).perform(click())
        onView(withId(R.id.table_scroll_view)).check(matches(isDisplayed()))
    }

    @Test
    fun shoudCreateTableViewAfterSwitchingbewteen() {
        onView(withId(R.id.btn_table))
        assertListNotEmpty(R.id.route_table)
    }
}