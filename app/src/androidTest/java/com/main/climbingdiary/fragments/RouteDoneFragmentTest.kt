package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestHelper.getRandomRoute
import com.main.climbingdiary.helper.TestHelper.hasItem
import com.main.climbingdiary.helper.TestHelper.translateDate
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
import org.hamcrest.CoreMatchers.not
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
                    0,
                    TestHelper.clickOnViewChild(R.id.route_edit)
                )
            )

        //change textfields
        onView(withId(R.id.input_route_name))
            .inRoot(RootMatchers.isDialog())
            .perform(replaceText(updateRoute.name))
        onView(withId(R.id.input_route_area))
            .inRoot(RootMatchers.isDialog())
            .perform(replaceText(updateRoute.area))
        onView(withId(R.id.input_route_sektor))
            .inRoot(RootMatchers.isDialog())
            .perform(replaceText(updateRoute.sector))
        onView(withId(R.id.input_route_sektor))
            .inRoot(RootMatchers.isDialog())
            .perform(replaceText(updateRoute.comment))
        onView(withId(R.id.input_route_date))
            .inRoot(RootMatchers.isDialog())
            .perform(replaceText(updateRoute.date))

        //Todo change Style, Date and Level

        //click update to setup the tick
        onView(withId(R.id.input_route_save))
            .inRoot(RootMatchers.isDialog())
            .check(matches(withText("Update")))
            .perform(scrollTo(), click())

        //check that updated values exists
        onView(withId(R.id.rvRoutes))
            .check(matches((hasItem(not(hasDescendant(withText(updateRoute.name)))))))
            .check(matches((hasItem(not(hasDescendant(withText(updateRoute.area)))))))
            .check(matches((hasItem(not(hasDescendant(withText(translateDate(updateRoute.date))))))))
            .check(matches((hasItem(not(hasDescendant(withText(updateRoute.sector)))))))
    }
}