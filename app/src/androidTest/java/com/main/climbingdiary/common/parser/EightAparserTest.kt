package com.main.climbingdiary.common.parser

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class EightAparserTest() {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun onCreate() {
        activityScenario.moveToState(Lifecycle.State.CREATED)
    }

    fun login() {
        val parser = EightAparser()
        parser.login()
    }
}