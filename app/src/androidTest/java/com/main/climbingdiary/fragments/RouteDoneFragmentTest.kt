package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDrawableDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertListItemCount
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItemChild
import com.adevinta.android.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.adevinta.android.barista.interaction.BaristaScrollInteractions.scrollTo
import com.adevinta.android.barista.interaction.BaristaSleepInteractions.sleep
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.RoutesAdapter.Companion.getRoutStyleIcon
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.clickOnViewChild
import com.main.climbingdiary.helper.TestHelper.getRandomRoute
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestHelper.translateDate
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.helper.TestProvider.changeInputTest
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestProvider.setSpinnerSelect
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.SportType
import com.main.climbingdiary.models.Tabs
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.time.LocalDate
import kotlin.random.Random.Default.nextInt

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
internal class RouteDoneFragmentTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val repo = RouteRepository(Route::class)
    private lateinit var route: Route
    private val maxRoutes: Int = 5
    private lateinit var routeList: List<Route>

    @After
    fun cleanUp() {
        cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario = initDefaultScenario()
        route = getRandomRoute()
        repo.insertRoute(route)
        routeList = getRandomRouteList(maxRoutes)
        routeList.forEach { repo.insertRoute(it) }
        TestProvider.openSportView(SportType.KLETTERN)
        openTab(Tabs.ROUTEN)
    }


    @Test
    fun updateRouteShouldUpdateAllFields() {
        val pos = nextInt(maxRoutes)
        val updateRoute = getRandomRoute()
        //select route and button to edit
        onView(withId(R.id.rvRoutes)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                pos,
                click()
            )
        )
        onView(withId(R.id.rvRoutes))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    pos, clickOnViewChild(R.id.route_edit)
                )
            )

        //change textfields
        changeInputTest(R.id.input_route_name, updateRoute.name!!)
        changeInputTest(R.id.input_route_area, updateRoute.area!!)
        changeInputTest(R.id.input_route_sektor, updateRoute.sector!!)
        //set the Stil
        setSpinnerSelect(R.id.input_route_level, updateRoute.level)
        setSpinnerSelect(R.id.input_route_stil, updateRoute.style)

        clickOn("Update")

        //check that updated values exists
        assertDisplayedAtPosition(R.id.rvRoutes, pos, R.id.route_name, updateRoute.name!!)
        assertContains(updateRoute.area!!)
        assertContains(updateRoute.sector!!)
        assertDisplayedAtPosition(R.id.rvRoutes, pos, R.id.route_level, updateRoute.level)
        assertDrawableDisplayedAtPosition(
            R.id.rvRoutes,
            pos,
            R.id.route_style,
            getRoutStyleIcon(updateRoute.style)
        )

        assertListItemCount(R.id.rvRoutes, maxRoutes + 1)
    }

    @Test
    @MediumTest
    fun createNewRouteOk() {
        val routeSet = getRandomRoute()
        routeSet.name="Mi Puó Fare Accendere "
        routeSet.level = "8a"
        routeSet.date = "${LocalDate.now().year}-12-12"
        //open add Project Button
        onView(withId(R.id.floating_action_btn_add)).perform(click())
        changeInputTest(R.id.input_route_name, routeSet.name!!)
        //fill the input fields
        changeInputTest(R.id.input_route_area, routeSet.area!!)
        changeInputTest(R.id.input_route_sektor, routeSet.sector!!)
        changeInputTest(R.id.input_route_comment, routeSet.comment!!)
        changeInputTest(R.id.input_route_date, routeSet.date!!)
        clickOn(R.id.grade_system_switcher)
        setSpinnerSelect(R.id.input_route_level, "IX+/X-")

        //select save
        clickOn(R.string.app_save)

        //check if List contains new project
        assertDisplayed(routeSet.name!!)
        assertContains(routeSet.area!!)
        assertContains(routeSet.sector!!)
        assertDisplayed(routeSet.level)
        assertDisplayed(translateDate(routeSet.date!!))
    }

    @Test
    @SmallTest
    fun deleteRouteOk() {
        val pos = 4
        assertRecyclerViewItemCount(R.id.rvRoutes, 6)
        clickListItemChild(R.id.rvRoutes, pos, R.id.route_delete)
        clickOn("Ok")
        clickOn("Ok")
        assertRecyclerViewItemCount(R.id.rvRoutes, 5)
    }
}