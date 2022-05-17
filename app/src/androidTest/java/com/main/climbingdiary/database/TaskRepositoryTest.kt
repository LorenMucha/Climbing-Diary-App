package com.main.climbingdiary.database

import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class TaskRepositoryTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private val repo = RouteRepository(Route::class)
    private lateinit var route: Route

    @After
    fun cleanUp() {
        cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = initDefaultScenario()
        route = TestHelper.getRandomRoute()
    }

    @Test
    fun saveRouteWithNonUTF8CharacterOk() {
        route.name = "Nejslabi, máte padáka"
        route.area = "Mi Puó Fare Accendere "
        route.sector = "Bílý Kůň"
        route.comment = "Généralités del`´s"
        repo.insertRoute(route)
    }
}