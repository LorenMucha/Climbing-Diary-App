package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.clickOnViewChild
import com.main.climbingdiary.helper.TestHelper.getRandomProjekt
import com.main.climbingdiary.helper.TestHelper.getRandomString
import com.main.climbingdiary.helper.TestHelper.hasItem
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.models.Levels
import com.main.climbingdiary.models.Styles
import org.hamcrest.CoreMatchers.*
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.Espresso.onData





internal class RouteProjectFragmentTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val repo = RouteRepository(Projekt::class)
    private val project = getRandomProjekt()

    @After
    fun cleanUp() {
        repo.deleteRoute(project)
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario =
            ActivityScenario.launch(com.main.climbingdiary.activities.MainActivity::class.java)
        repo.insertRoute(project)
    }

    @Test
    @LargeTest
    fun tickedProjectShutShownInRouteDoneView() {
        //open project tab
        TestProvider.openProjectTab()
        //click checkbox to tick project
        onView(withId(R.id.rvProjekte))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                (0, clickOnViewChild(R.id.tick_project)))
        //click update to setup the tick
        onView(withId(R.id.input_route_save))
            .inRoot(isDialog())
            .check(matches(withText("Ticken")))
            .perform(scrollTo(),click())
        //check if the route is included in the List for route done
        onView(withId(R.id.rvRoutes))
            .check(matches(hasItem(hasDescendant(withText("TestRoute")))))
        //go back to project
        TestProvider.openProjectTab()
        //check that project not exists anymore inside the list
        onView(withId(R.id.rvProjekte))
            .check(matches((hasItem(not(hasDescendant(withText(project.name)))))))
            .check(matches((hasItem(not(hasDescendant(withText(project.area)))))))
            .check(matches((hasItem(not(hasDescendant(withText(project.sector)))))))
            .check(matches((hasItem(not(hasDescendant(withText(project.level)))))))
    }

    @Test
    @MediumTest
    fun addNewProjectToList(){
        TestProvider.openProjectTab()
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
            .perform(scrollTo(),click())
        //check if List contains new project
        onView(withId(R.id.rvProjekte))
            .check(matches((hasItem(not(hasDescendant(withText(project.name)))))))
            .check(matches((hasItem(not(hasDescendant(withText(project.area)))))))
            .check(matches((hasItem(not(hasDescendant(withText(project.sector)))))))
    }
}