package com.main.climbingdiary.controller

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.assertion.BaristaAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaDialogInteractions
import com.adevinta.android.barista.interaction.BaristaDrawerInteractions.openDrawer
import com.adevinta.android.barista.interaction.BaristaScrollInteractions
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.models.Tabs
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class NavDrawerControllerTest{
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun cleanUp() {
        activityScenario.close()
        Intents.release()
    }

    @Before
    fun setUp(){
        activityScenario = initDefaultScenario()
        Intents.init()
    }

    @Test
    @SmallTest
    fun openDrawerAndNavigateToBouldernOk(){
        openDrawer()
        clickOn(R.string.nav_title_bouldern)
        clickOn(Tabs.BOULDER.typeToString())
    }

    @Test
    @SmallTest
    fun openDrawerAndNavigateToClimbingOk(){
        openDrawer()
        clickOn(R.string.nav_title_climbing)
        clickOn(Tabs.ROUTEN.typeToString())
    }

    @Test
    @SmallTest
    fun openDrawerAndNavigateToSettingsOk(){
        openDrawer()
        clickOn(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.nav_title_settings))
        assertContains(TestProvider.getLocaleStringResource(Locale.GERMAN,R.string.safty_copy_pref_header))
    }
}