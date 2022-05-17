package com.main.climbingdiary.fragments

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickBack
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.BaristaScrollInteractions.scrollTo
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.LanguageManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestHelper.getRandomProjektList
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
import com.main.climbingdiary.models.TimeRange
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.util.*

//Todo: refactor setup method
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

    fun setUp() {
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
        activityScenario = initDefaultScenario()
        setUp()
        clickOn(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.backup_db_title))
        assertContains(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.alert_db_export_correctly))
        clickOn(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.app_ok))
    }

    @Ignore("Needs to be implemented")
    @Test
    @SmallTest
    fun restoreDataBaseOk() {
        activityScenario = initDefaultScenario()
        setUp()
        clickOn("Datenbank wiederherstellen")
        TODO()
    }

    @Test
    @SmallTest
    fun changeToTimeRangeOk() {
        activityScenario = initDefaultScenario()
        setUp()
        AppPreferenceManager.setTimeSliderView(TimeRange.YEAR)
        //Todo: noch in englisch
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
        activityScenario = initDefaultScenario()
        setUp()
        AppPreferenceManager.setTimeSliderView(TimeRange.RANGE)
        clickOn("Time-Slider")
        clickOn("Jahr")
        clickBack()
        openTab(Tabs.ROUTEN)
        clickOn(R.id.showTimeSlider)
        assertDisplayed(R.id.timeslider)
    }

    @Test
    fun switchLanguage() {
        TestHelper.setPreferenceString(PreferenceKeys.FIRST_TIME_LANGUAGE, false)
        TestHelper.setPreferenceString(PreferenceKeys.LANGUAGE, LanguageManager.DE)
        activityScenario = ActivityScenario
            .launch(MainActivity::class.java)
        clickMenu(R.id.app_settings)
        onView(withText(R.string.pref_language))
            .perform(swipeUp())
        clickOn(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.language_switch_title))
        assertContains(
            TestProvider.getLocaleStringResource(
                Locale.ENGLISH,
                R.string.alert_language_change
            )
        )
    }
}