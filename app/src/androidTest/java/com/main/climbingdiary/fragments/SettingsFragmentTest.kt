package com.main.climbingdiary.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.getRandomProjektList
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestSqliteHelper
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class SettingsFragmentTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val routeRepo = RouteRepository(Route::class)
    private val projektRepo = RouteRepository(Projekt::class)

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
        getRandomRouteList(50).forEach { routeRepo.insertRoute(it) }
        getRandomProjektList(10).forEach { projektRepo.insertRoute(it) }
        AppPreferenceManager.setOutputPath("storage/emulated/0/Download")
        clickMenu(R.id.app_settings)
    }

    @Test
    @LargeTest
    fun dataBaseExportWorks() {
        clickOn("Sicherungskopie")
        clickOn("Ok")
        clickOn("Wiederherstellung")
    }

}