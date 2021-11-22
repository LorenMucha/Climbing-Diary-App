package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.clickOnViewChild
import com.main.climbingdiary.helper.TestHelper.getRandomRoute
import com.main.climbingdiary.helper.TestHelper.getRandomRouteList
import com.main.climbingdiary.helper.TestProvider.changeInputTest
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestProvider.setSpinnerSelect
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
import org.junit.After
import org.junit.Before
import org.junit.Test


internal class RouteDoneFragmentTest {
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
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
        route = getRandomRoute()
        repo.insertRoute(route)
    }

    @Test
    @LargeTest
    fun updateRouteShouldUpdateAllFields() {
        val updateRoute = getRandomRoute()
        //open View
        openTab(Tabs.ROUTEN)
        //select route and button to edit
        onView(withId(R.id.rvRoutes))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        onView(withId(R.id.rvRoutes))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, clickOnViewChild(R.id.route_edit)
                )
            )

        //change textfields
        changeInputTest(R.id.input_route_name, updateRoute.name!!)
        changeInputTest(R.id.input_route_area, updateRoute.area!!)
        changeInputTest(R.id.input_route_sektor, updateRoute.sector!!)
        //set the Stil
        setSpinnerSelect(R.id.input_route_level, updateRoute.level)

        //click update to setup the tick
        onView(withId(R.id.input_route_save))
            .inRoot(RootMatchers.isDialog())
            .perform(scrollTo(), click())

        //check that updated values exists
        assertDisplayed(updateRoute.name!!)
        assertContains(updateRoute.sector!!)
        assertContains(updateRoute.area!!)
        assertDisplayed(updateRoute.level)
    }
}