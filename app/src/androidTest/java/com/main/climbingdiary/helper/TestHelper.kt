package com.main.climbingdiary.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.BoundedMatcher
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.models.Levels
import com.main.climbingdiary.models.Styles
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random.Default.nextInt


object TestHelper {

    fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
    }

    fun hasItem(matcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item: ")
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val adapter = view.adapter
                for (position in 0 until adapter!!.itemCount) {
                    val type = adapter.getItemViewType(position)
                    val holder = adapter.createViewHolder(view, type)
                    adapter.onBindViewHolder(holder, position)
                    if (matcher.matches(holder.itemView)) {
                        return true
                    }
                }
                return false
            }
        }
    }

    fun getRandomString(length: Int): String {
        return RandomStringUtils.randomAlphanumeric(length)
    }

    fun getRandomProjekt(): Projekt {
        val levels = Levels.getLevelsFrench()
        val rating = nextInt(5)
        return Projekt(
            0,
            levels.get(nextInt(levels.size)),
            getRandomString(8),
            getRandomString(12),
            getRandomString(10),
            rating,
            getRandomString(25)
        )
    }

    fun getRandomRoute(): Route {
        val date = getRandomDate()
        val levels = Levels.getLevelsFrench()
        val rating = nextInt(5)
        val styles = listOf<String>(Styles.getFLASH(), Styles.getRP(), Styles.getOS())
        return Route(
            0,
            styles.get(nextInt(styles.size)),
            levels.get(nextInt(levels.size)),
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

    fun getRandomRouteList(max: Int = 500): Array<Route> {
        val routeArray = emptyArray<Route>()
        for (i in 1..max) {
            val route = getRandomRoute()
            routeArray[i] = route
        }
        return routeArray
    }
}