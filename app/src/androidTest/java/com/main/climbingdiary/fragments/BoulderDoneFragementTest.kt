package com.main.climbingdiary.fragments

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.adevinta.android.barista.assertion.BaristaListAssertions
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaClickInteractions
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItemChild
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.RoutesAdapter
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.helper.TestHelper
import com.main.climbingdiary.helper.TestProvider
import com.main.climbingdiary.helper.TestProvider.openSportView
import com.main.climbingdiary.helper.TestProvider.openTab
import com.main.climbingdiary.helper.TestSqliteHelper
import com.main.climbingdiary.helper.TestSqliteHelper.cleanAllTables
import com.main.climbingdiary.models.SportType
import com.main.climbingdiary.models.Tabs
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.time.LocalDate
import kotlin.random.Random

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
internal class BoulderDoneFragementTest {
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    private val repo = RouteRepository(Route::class)
    private lateinit var route: Route
    private val maxBoulders: Int = 5
    private lateinit var boulderList: List<Route>

    @After
    fun cleanUp() {
        cleanAllTables()
        activityScenario.close()
    }

    @Before
    fun setUp() {
        activityScenario =
            ActivityScenario.launch(MainActivity::class.java)
        route = TestHelper.getRandomRoute()
        repo.insertRoute(route)
        boulderList = TestHelper.getRandomRouteList(maxBoulders)
        boulderList.forEach { repo.insertRoute(it) }
        openSportView(SportType.BOULDERN)
        openTab(Tabs.BOULDER)
    }

    @Test
    fun updateBoulderShouldUpdateAllFields() {
        val pos = Random.nextInt(maxBoulders)
        val updateBoulder = TestHelper.getRandomRoute()
        //select route and button to edit
        Espresso.onView(ViewMatchers.withId(R.id.rvRoutes)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                pos,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.rvRoutes))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    pos, TestHelper.clickOnViewChild(R.id.route_edit)
                )
            )

        //change textfields
        TestProvider.changeInputTest(R.id.input_route_name, updateBoulder.name!!)
        TestProvider.changeInputTest(R.id.input_route_area, updateBoulder.area!!)
        TestProvider.changeInputTest(R.id.input_route_sektor, updateBoulder.sector!!)
        //set the Stil
        TestProvider.setSpinnerSelect(R.id.input_route_level, updateBoulder.level)
        TestProvider.setSpinnerSelect(R.id.input_route_stil, updateBoulder.style)

        BaristaClickInteractions.clickOn("Update")

        //check that updated values exists
        BaristaListAssertions.assertDisplayedAtPosition(
            R.id.rvRoutes,
            pos,
            R.id.route_name,
            updateBoulder.name!!
        )
        BaristaVisibilityAssertions.assertContains(updateBoulder.area!!)
        BaristaVisibilityAssertions.assertContains(updateBoulder.sector!!)
        BaristaListAssertions.assertDisplayedAtPosition(
            R.id.rvRoutes,
            pos,
            R.id.route_level,
            updateBoulder.level
        )
        BaristaListAssertions.assertDrawableDisplayedAtPosition(
            R.id.rvRoutes,
            pos,
            R.id.route_style,
            RoutesAdapter.getRoutStyleIcon(updateBoulder.style)
        )

        BaristaListAssertions.assertListItemCount(R.id.rvRoutes, maxBoulders + 1)
    }

    @Test
    @MediumTest
    fun createNewBoulderOk() {
        val boulderSet = TestHelper.getRandomRoute()
        boulderSet.level = "8a"
        boulderSet.date = "${LocalDate.now().year}-12-12"
        //open add Project Button
        Espresso.onView(ViewMatchers.withId(R.id.floating_action_btn_add))
            .perform(ViewActions.click())
        TestProvider.changeInputTest(R.id.input_route_name, boulderSet.name!!)
        //fill the input fields
        TestProvider.changeInputTest(R.id.input_route_area, boulderSet.area!!)
        TestProvider.changeInputTest(R.id.input_route_sektor, boulderSet.sector!!)
        TestProvider.changeInputTest(R.id.input_route_comment, boulderSet.comment!!)
        TestProvider.changeInputTest(R.id.input_route_date, boulderSet.date!!)
        TestProvider.setSpinnerSelect(R.id.input_route_level, boulderSet.level)

        //select save
        BaristaClickInteractions.clickOn("Speichern")

        //check if List contains new project
        BaristaVisibilityAssertions.assertDisplayed(boulderSet.name!!)
        BaristaVisibilityAssertions.assertContains(boulderSet.area!!)
        BaristaVisibilityAssertions.assertContains(boulderSet.sector!!)
        BaristaVisibilityAssertions.assertDisplayed(boulderSet.level)
        BaristaVisibilityAssertions.assertDisplayed(TestHelper.translateDate(boulderSet.date!!))
    }

    @Test
    @SmallTest
    fun deleteBoulderOk() {
        val pos = 4
        assertRecyclerViewItemCount(R.id.rvRoutes, 6)
        clickListItemChild(R.id.rvRoutes, pos, R.id.route_delete)
        clickOn("Ok")
        clickOn("Ok")
        assertRecyclerViewItemCount(R.id.rvRoutes, 5)
    }
}