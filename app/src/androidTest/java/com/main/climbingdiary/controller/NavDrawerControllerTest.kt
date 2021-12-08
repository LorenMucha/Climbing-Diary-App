package com.main.climbingdiary.controller

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import com.main.climbingdiary.activities.MainActivity
import org.junit.After
import org.junit.Before

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

}