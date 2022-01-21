package com.main.climbingdiary.fragments

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickBack
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.PermissionGranter
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.getRandomProjektList
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
import com.main.climbingdiary.models.TimeRange
import org.junit.*


internal class SettingsFragmentTest {


    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val routeRepo = RouteRepository(Route::class)
    private val projektRepo = RouteRepository(Projekt::class)
    private val dbOutputPath = "storage/emulated/0/Download"

    @After
    fun cleanUp() {
        cleanAllTables()
        AppPreferenceManager.setTimeSliderView(TimeRange.YEAR)
        activityScenario.close()
    }

    @Before
    fun setUp() {
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.READ_EXTERNAL_STORAGE)
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
        cleanAllTables()
        AppPreferenceManager.setOutputPath(dbOutputPath)
        //init
        getRandomRouteList(15).forEach { routeRepo.insertRoute(it) }
        getRandomProjektList(5).forEach { projektRepo.insertRoute(it) }
        clickMenu(R.id.app_settings)

    }

    @Test
    @SmallTest
    fun dataBaseExportOk() {
        clickOn("Sicherungskopie erstellen")
        assertDisplayed("Die Datenbank wurde exportiert !")
        clickOn("Ok")
    }

    @Ignore("Needs to be implemented")
    @Test
    @SmallTest
    fun restoreDataBaseOk() {
        clickOn("Datenbank wiederherstellen")
        TODO()
    }

    @Test
    @SmallTest
    fun changeToTimeRangeOk() {
        AppPreferenceManager.setTimeSliderView(TimeRange.YEAR)
        clickOn("Time-Slider")
        clickOn("Zeitraum")
        clickBack()
        openTab(Tabs.ROUTEN)
        clickOn(R.id.showTimeSlider)
        assertDisplayed(R.id.timerange)
    }

    @Test
    @SmallTest
    fun changeToYearOk() {
        AppPreferenceManager.setTimeSliderView(TimeRange.RANGE)
        clickOn("Time-Slider")
        clickOn("Jahr")
        clickBack()
        openTab(Tabs.ROUTEN)
        clickOn(R.id.showTimeSlider)
        assertDisplayed(R.id.timeslider)
    }
}