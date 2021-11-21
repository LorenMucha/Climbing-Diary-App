package com.main.climbingdiary.helper

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.models.Tabs
import org.hamcrest.core.AllOf

object TestProvider {
    fun openTab(tabvalue: Tabs){
        val tabMap = mapOf(Tabs.PROJEKTE to 2, Tabs.STATISTIK to 0, Tabs.ROUTEN to 1)
        Espresso.onView(ViewMatchers.withId(R.id.tabLayout)).perform(
            selectTabAtPosition(
                tabMap.get(tabvalue)
            )
        )
    }

    private fun selectTabAtPosition(tabIndex: Int?): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() =
                AllOf.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(TabLayout::class.java)
                )

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex!!)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}