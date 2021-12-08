package com.main.climbingdiary.controller

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDrawerInteractions.openDrawer
import com.main.climbingdiary.activities.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

class NavDrawerControllerTest{
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
    @SmallTest
    fun openDrawerAndNavigateToBouldernOk(){
        openDrawer()
        clickOn("Bouldern")
        clickOn("Boulder")
    }

    @Test
    @SmallTest
    fun openDrawerAndNavigateToClimbingOk(){
        openDrawer()
        clickOn("Klettern")
        clickOn("Routen")
    }
}