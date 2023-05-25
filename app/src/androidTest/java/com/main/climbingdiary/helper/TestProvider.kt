package com.main.climbingdiary.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDrawerInteractions
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.models.SportType
import com.main.climbingdiary.models.Tabs
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AllOf
import java.util.*


object TestProvider {
    fun openTab(tabvalue: Tabs) {
        val setter = if (tabvalue == Tabs.BOULDER) Tabs.ROUTEN else tabvalue
        val tabMap = mapOf(Tabs.PROJEKTE to 2, Tabs.STATISTIK to 0, Tabs.ROUTEN to 1)
        onView(ViewMatchers.withId(R.id.tabLayout)).perform(
            selectTabAtPosition(
                tabMap[setter]
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

    inline fun waitUntilLoaded(crossinline recyclerProvider: () -> RecyclerView) {
        Espresso.onIdle()

        lateinit var recycler: RecyclerView

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recycler = recyclerProvider()
        }

        while (recycler.hasPendingAdapterUpdates()) {
            Thread.sleep(10)
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

    fun openSportView(sportType: SportType) {
        BaristaDrawerInteractions.openDrawer()
        when (sportType) {
            SportType.BOULDERN -> clickOn(R.string.nav_title_bouldern)
            SportType.KLETTERN -> clickOn(R.string.nav_title_climbing)
        }
    }

    fun getLocaleStringResource(
        requestedLocale: Locale,
        resourceId: Int
    ): String {
        val result: String
        val context:Context = InstrumentationRegistry.getInstrumentation().targetContext
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { // use latest api
            val config = Configuration(context.resources.configuration)
            config.setLocale(requestedLocale)
            result = context.createConfigurationContext(config).getText(resourceId).toString()
        } else { // support older android versions
            val resources: Resources = context.resources
            val conf: Configuration = resources.configuration
            val savedLocale: Locale = conf.locale
            conf.locale = requestedLocale
            resources.updateConfiguration(conf, null)

            // retrieve resources from desired locale
            result = resources.getString(resourceId)

            // restore original locale
            conf.locale = savedLocale
            resources.updateConfiguration(conf, null)
        }
        return result
    }
}