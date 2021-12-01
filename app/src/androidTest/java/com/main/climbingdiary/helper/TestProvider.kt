package com.main.climbingdiary.helper

import android.os.Build
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.models.Tabs
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AllOf


object TestProvider {
    fun openTab(tabvalue: Tabs) {
        val tabMap = mapOf(Tabs.PROJEKTE to 2, Tabs.STATISTIK to 0, Tabs.ROUTEN to 1)
        onView(ViewMatchers.withId(R.id.tabLayout)).perform(
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

    fun changeInputTest(id: Int, string: String) {
        onView(ViewMatchers.withId(id))
            .perform(ViewActions.replaceText(string))
    }

    fun setSpinnerSelect(id: Int, choice: String) {
        onView(ViewMatchers.withId(id))
            .perform(ViewActions.click())

        onData(
            allOf(
                `is`(CoreMatchers.instanceOf(String::class.java)),
                `is`(choice)
            )
        )
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(ViewActions.click())
    }
    fun grantPermission() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = UiDevice.getInstance(instrumentation).findObject(
                UiSelector().text(
                    when {
                        Build.VERSION.SDK_INT == 23 -> "Allow"
                        Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                        Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                        else -> "While using the app"
                    }
                )
            )
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }
    }
}