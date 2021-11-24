package com.main.climbingdiary.activities

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.main.climbingdiary.R
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class MainActivityTest {

    @Test
    fun onCreate() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.CREATED)
    }

    @Test
    fun onSettingsClick() {
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        Intents.init()
        onView(withText(R.string.settings_title)).perform(click())
        intended(hasComponent(hasClassName(SettingsActivity::class.java.name)))
    }
}