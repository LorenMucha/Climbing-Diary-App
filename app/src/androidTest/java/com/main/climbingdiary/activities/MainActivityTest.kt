package com.main.climbingdiary.activities

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.main.climbingdiary.R
import com.main.climbingdiary.fragments.StatisticFragment
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

internal class MainActivityTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
        Intents.release()
    }

    @Before
    fun setUp(){
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        Intents.init()
    }

    @Test
    fun onCreate() {
        activityScenario.moveToState(Lifecycle.State.CREATED)
    }

    @Test
    fun shouldOpenSettingsFragmentOnSettingsClick() {
        Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.settings_title)).perform(click())
        intended(hasComponent(hasClassName(SettingsActivity::class.java.name)))
    }
}