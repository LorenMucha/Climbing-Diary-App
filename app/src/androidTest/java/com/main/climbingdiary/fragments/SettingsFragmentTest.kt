package com.main.climbingdiary.fragments

import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository

internal class SettingsFragmentTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val routeRepo = RouteRepository(Route::class)
    private val projektRepo = RouteRepository(Projekt::class)

    
}