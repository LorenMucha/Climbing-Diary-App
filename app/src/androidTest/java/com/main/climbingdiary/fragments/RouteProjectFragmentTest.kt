package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDrawableDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertContains
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItemChild
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.RoutesAdapter.Companion.getRoutStyleIcon
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper.clickOnViewChild
import com.main.climbingdiary.helper.TestHelper.getRandomProjekt
import com.main.climbingdiary.helper.TestHelper.initDefaultScenario
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.helper.TestProvider.changeInputTest
import com.main.climbingdiary.helper.TestProvider.setSpinnerSelect
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.Tabs
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
        activityScenario = initDefaultScenario()
        project = getRandomProjekt()
        repo.insertRoute(project)
        TestProvider.openTab(Tabs.PROJEKTE)
    }

    @Test
    @LargeTest
    fun tickedProjectShutShownInRouteDoneView() {
        //click checkbox to tick project
        onView(withId(R.id.rvProjekte))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>
                    (0, clickOnViewChild(R.id.tick_project))
            )
        //click update to setup the tick
        clickOn("Ticken")
        assertDisplayed("Stark!")
        clickOn("Ok")

        //check if the route is included in the List for route done
        assertDisplayedAtPosition(R.id.rvRoutes, 0, R.id.route_name, project.name!!)
        assertContains(project.area!!)
        assertContains(project.sector!!)
        assertContains(project.sector!!)
        assertDisplayedAtPosition(R.id.rvRoutes, 0, R.id.route_level, project.level)
        assertDrawableDisplayedAtPosition(
            R.id.rvRoutes,
            0,
            R.id.route_style,
            getRoutStyleIcon("RP")
        )

        //go back to project
        TestProvider.openTab(Tabs.PROJEKTE)

        //check that project not exists anymore inside the list
        assertNotDisplayed(project.name!!)
    }

    @Test
    @MediumTest
    fun addNewProjectToList() {
        val projectSet = getRandomProjekt()
        //open add Project Button
        onView(withId(R.id.floating_action_btn_add)).perform(click())
        //fill the input fields
        changeInputTest(R.id.input_route_name, projectSet.name!!)
        changeInputTest(R.id.input_route_area, projectSet.area!!)
        changeInputTest(R.id.input_route_sektor, projectSet.sector!!)
        changeInputTest(R.id.input_route_comment, projectSet.comment!!)

        setSpinnerSelect(R.id.input_route_level, projectSet.level)

        //select save
        clickOn("Speichern")

        //check if List contains new project
        assertDisplayed(projectSet.name!!)
        assertContains(projectSet.area!!)
        assertContains(projectSet.sector!!)
        assertDisplayed(projectSet.level)
    }

    @Test
    @SmallTest
    fun deleteProjektOk() {
        assertRecyclerViewItemCount(R.id.rvProjekte, 1)
        clickListItemChild(R.id.rvProjekte, 0, R.id.route_delete)
        clickOn("Ok")
        clickOn("Ok")
        assertRecyclerViewItemCount(R.id.rvProjekte, 0)
    }
}