package com.main.climbingdiary.fragments

import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.getRandomProjektList
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestSqliteHelper
import org.junit.After
import org.junit.Before

internal class SettingsFragmentTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val routeRepo = RouteRepository(Route::class)
    private val projektRepo = RouteRepository(Projekt::class)

    @After
    fun cleanUp() {
        TestSqliteHelper.cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
        getRandomRouteList(50).forEach { routeRepo.insertRoute(it) }
        getRandomProjektList(10).forEach { projektRepo.insertRoute(it) }
    }


}