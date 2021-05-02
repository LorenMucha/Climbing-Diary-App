package com.main.climbingdiary

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.model.SportType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceManagerTest {

    private var prefManager: AppPreferenceManager =
        AppPreferenceManager(InstrumentationRegistry.getInstrumentation().targetContext)


    @Test
    fun setSportTypeTest() {
        prefManager.setSportType(SportType.BOULDERN)
        assertEquals(prefManager.getSportType(), SportType.BOULDERN)
        prefManager.setSportType(SportType.KLETTERN)
        assertEquals(prefManager.getSportType(),SportType.KLETTERN)
    }
}