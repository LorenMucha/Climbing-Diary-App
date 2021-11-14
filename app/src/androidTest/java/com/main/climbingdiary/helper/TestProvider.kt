package com.main.climbingdiary.helper

import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import com.main.climbingdiary.R

object TestProvider {
    fun openProjectTab(){
        Espresso.onView(ViewMatchers.withId(R.id.tabLayout)).perform(
            TestHelper.selectTabAtPosition(
                2
            )
        )
    }
}