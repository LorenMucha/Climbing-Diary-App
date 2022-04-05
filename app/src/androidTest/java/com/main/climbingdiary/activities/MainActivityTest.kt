package com.main.climbingdiary.activities

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions
import com.main.climbingdiary.R
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        BaristaMenuClickInteractions.clickMenu(R.id.app_settings)
        intended(hasComponent(hasClassName(SettingsActivity::class.java.name)))
    }
}