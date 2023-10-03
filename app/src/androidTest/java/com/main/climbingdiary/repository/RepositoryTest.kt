package com.main.climbingdiary.repository

import androidx.test.core.app.ActivityScenario
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.database.entities.SectorRepository
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.math.log

internal class RepositoryTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private val routeRepo = RouteRepository(Route::class)
    private lateinit var route: Route

    @After
    fun cleanUp() {
        cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = initDefaultScenario()
    }

    @Test
    fun saveRouteAndSectorAndAreaAreSavedOk() {
        var route = Route(
            name = "Schmarotzer",
            area = "Vorarlberg",
            sector = "Bürser Schlucht",
            rating = 3,
            date = TestHelper.getRandomDate()
        )

        routeRepo.insertRoute(route)

        var list = SectorRepository.getSectorList("Vorarlberg")
        assertEquals(list
            [0], route.sector)
        //repo.getRoute(route.id)
    }
}