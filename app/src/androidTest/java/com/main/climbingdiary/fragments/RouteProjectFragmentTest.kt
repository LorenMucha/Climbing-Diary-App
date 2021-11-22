package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.adevinta.android.barista.internal.viewaction.SleepViewAction.sleep
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.clickOnViewChild
import com.main.climbingdiary.helper.TestHelper.getRandomProjekt
import com.main.climbingdiary.helper.TestHelper.hasItem
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class RouteProjectFragmentTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val repo = RouteRepository(Projekt::class)
    private lateinit var project: Projekt

    @After
    fun cleanUp() {
        cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario =
            ActivityScenario.launch(com.main.climbingdiary.activities.MainActivity::class.java)
        project = getRandomProjekt()
        repo.insertRoute(project)
    }

    @Test
    @LargeTest
    fun tickedProjectShutShownInRouteDoneView() {
        //open project tab
        TestProvider.openTab(Tabs.PROJEKTE)
        //click checkbox to tick project
        onView(withId(R.id.rvProjekte))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                    (0, clickOnViewChild(R.id.tick_project))
            )
        //click update to setup the tick
        onView(withId(R.id.input_route_save))
            .inRoot(isDialog())
            .check(matches(withText("Ticken")))
            .perform(scrollTo(), click())
        //check if the route is included in the List for route done
        assertDisplayed(project.name!!)

        //go back to project
        TestProvider.openTab(Tabs.PROJEKTE)

        //check that project not exists anymore inside the list
        assertNotDisplayed(project.name!!)
    }

    @Test
    @MediumTest
    fun addNewProjectToList() {
        TestProvider.openTab(Tabs.PROJEKTE)
        //open add Project Button
        onView(withId(R.id.floating_action_btn_add)).perform(click())
        //fill the input fields
        onView(withId(R.id.input_route_name))
            .inRoot(isDialog())
            .perform(typeText(project.name))
        onView(withId(R.id.input_route_area))
            .inRoot(isDialog())
            .perform(typeText(project.area))
        onView(withId(R.id.input_route_sektor))
            .inRoot(isDialog())
            .perform(typeText(project.sector))

        //select save
        onView(withId(R.id.input_route_save))
            .inRoot(isDialog())
            .check(matches(withText("Speichern")))
            .perform(scrollTo(), click())
        //check if List contains new project
        assertDisplayed(project.name!!)
        assertContains(project.sector!!)
        assertContains(project.area!!)
        assertDisplayed(project.level)
    }
}