package com.main.climbingdiary.showcase

import androidx.test.core.app.ActivityScenario
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotContains
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class ShowCaseProviderTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        AppPreferenceManager.setIsUsedFirstTime(false)
    }

    @Test
    fun createShowCase() {
        assertContains(getString(R.string.showcase_app_welcome))
        assertContains(getString(R.string.showcase_question_text_show_case))
        clickOn(getString(R.string.showcase_gerne))
        assertContains(getString(R.string.showcase_header))
        assertContains(getString(R.string.showcase_info))
        clickOn(getString(R.string.showcase_further))
        assertContains(getString(R.string.showcase_addNewRouteHeader))
        assertContains(getString(R.string.showcase_addNewRouteText))
        clickOn(getString(R.string.showcase_further))
        assertContains(getString(R.string.showcase_searchBarHeader))
        assertContains(getString(R.string.showcase_searchBarText))
        clickOn(getString(R.string.showcase_further))
        assertContains(getString(R.string.showcase_filterBarHeader))
        assertContains(getString(R.string.showcase_filterBarText))
        clickOn(getString(R.string.showcase_further))
    }

    @Test
    fun createShowCaseAndCancel() {
        assertContains(getString(R.string.showcase_app_welcome))
        assertContains(getString(R.string.showcase_question_text_show_case))
        clickOn("Nein")
        assertNotContains(getString(R.string.showcase_app_welcome))
        assertNotContains(getString(R.string.showcase_question_text_show_case))
    }
}