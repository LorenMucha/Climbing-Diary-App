package com.main.climbingdiary.helper

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.preference.PreferenceManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.interaction.PermissionGranter
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.LanguageManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.PreferenceKeys
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.models.Levels
import com.main.climbingdiary.models.Styles
import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt


object TestHelper {

    fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
    }

    fun getRandomString(length: Int): String {
        return RandomStringUtils.randomAlphanumeric(length)
    }

    fun getRandomProjekt(): Projekt {
        val levels = Levels.getLevelsFrench()
        val rating = nextInt(5)
        return Projekt(
            0,
            levels[nextInt(levels.size)],
            getRandomString(5),
            getRandomString(5),
            getRandomString(5),
            rating,
            getRandomString(5)
        )
    }

    fun getRandomRoute(): Route {
        val date = getRandomDate()
        val levels = Levels.getLevelsFrench()
        val rating = nextInt(5)
        val styles = listOf(Styles.getFLASH(), Styles.getRP(), Styles.getOS())
        return Route(
            0,
            styles[nextInt(styles.size)],
            levels[nextInt(levels.size)],
            getRandomString(8),
            getRandomString(12),
            getRandomString(10),
            rating,
            getRandomString(25),
            date
        )
    }

    fun getRandomDate(): String {
        val minDay = LocalDate.of(1970, 1, 1).toEpochDay()
        val maxDay = LocalDate.of(LocalDate.now().year, 12, 31).toEpochDay()
        val randomDay: Long = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        val randomDate = LocalDate.ofEpochDay(randomDay)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return formatter.format(randomDate)
    }

    fun getRandomRouteList(max: Int = 500): List<Route> {
        val routeArray: MutableList<Route> = ArrayList()
        for (i in 1..max) {
            val route = getRandomRoute()
            routeArray.add(route)
        }
        return routeArray
    }

    fun getRandomProjektList(max: Int = 500): List<Projekt> {
        val routeArray: MutableList<Projekt> = ArrayList()
        for (i in 1..max) {
            val projekt = getRandomProjekt()
            routeArray.add(projekt)
        }
        return routeArray
    }

    fun translateDate(date: String?): String {
        val dateList = date!!.split("-")
        return "${dateList[2]}.${dateList[1]}.${dateList[0]}"
    }

    fun setPreferenceString(prefKey: String, preference: Any) {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation().targetContext)
        val editor = prefs.edit()
        preference.let {
            if (preference is String) {
                editor.putString(prefKey, it as String)
            } else {
                editor.putBoolean(prefKey, it as Boolean)
            }
        }
        editor.apply()
    }

    fun cleanAllPreferences(){
        PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getInstrumentation()
            .targetContext)
            .edit()
            .clear()
            .apply()
    }

    fun initDefaultScenario(languageFirstTime:Boolean=false, isUsedFirstTime:Boolean=false):ActivityScenario<MainActivity>{
        //Fixme: PermissionGranter Not Working
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.READ_EXTERNAL_STORAGE)
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        setPreferenceString(PreferenceKeys.FIRST_TIME_LANGUAGE, languageFirstTime)
        setPreferenceString(PreferenceKeys.LANGUAGE, LanguageManager.DE)
        setPreferenceString(PreferenceKeys.FIRST_TIME, isUsedFirstTime)
        return ActivityScenario.launch(MainActivity::class.java)
    }
}