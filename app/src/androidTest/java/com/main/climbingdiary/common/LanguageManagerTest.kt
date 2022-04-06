package com.main.climbingdiary.common

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.interaction.BaristaClickInteractions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDialogInteractions
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.math.log

internal class LanguageManagerTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
        TestHelper.cleanAllPreferences()
    }

    @Test
    fun setLanguageFirstTimeDE() {
        activityScenario = TestHelper.initDefaultScenario(languageFirstTime = true)
        clickOn(R.id.btn_welcome_de)
        assertContains(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.app_will_restart))
        assertContains(getString(R.string.app_ok))
    }

    @Test
    fun setLanguageFirstTimeEN() {
        activityScenario = TestHelper.initDefaultScenario(languageFirstTime = true)
        clickOn(R.id.btn_welcome_en)
        assertContains(TestProvider.getLocaleStringResource(Locale.ENGLISH,R.string.app_will_restart))
        assertContains(getString(R.string.app_ok))
    }

}