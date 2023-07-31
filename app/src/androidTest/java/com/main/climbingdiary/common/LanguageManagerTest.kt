package com.main.climbingdiary.common

import androidx.test.core.app.ActivityScenario
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

internal class LanguageManagerTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun init(){
        activityScenario = TestHelper.initDefaultScenario(languageFirstTime = true)
    }

    @After
    fun cleanUp() {
        activityScenario.close()
        TestHelper.cleanAllPreferences()
    }

    @Test
    fun setLanguageFirstTimeDE() {
        assertContains(R.string.dialog_langoage_choose_header)
        clickOn(R.id.btn_welcome_de)
        assertContains(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.app_will_restart))
        assertContains(getString(R.string.app_ok))
    }

    @Test
    fun setLanguageFirstTimeEN() {
        assertContains(R.string.dialog_langoage_choose_header)
        clickOn(R.id.btn_welcome_en)
        assertContains(TestProvider.getLocaleStringResource(Locale.ENGLISH,R.string.app_will_restart))
        assertContains(getString(R.string.app_ok))
    }

}