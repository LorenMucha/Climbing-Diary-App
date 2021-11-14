package com.main.climbingdiary.helper

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.tabs.TabLayout
import org.hamcrest.core.AllOf.allOf
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso

import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.main.climbingdiary.R
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.models.Levels
import com.main.climbingdiary.models.Styles
import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.BaseMatcher
import java.util.*
import kotlin.random.Random.Default.nextInt


object TestHelper {
    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() =
                allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
    fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
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
    fun getRandomString(length:Int):String{
        return RandomStringUtils.randomAlphanumeric(length)
    }
    fun getRandomProjekt():Projekt{
        val levels = Levels.getLevelsFrench()
        val rating = nextInt(5)
        return Projekt(0,
            levels.get(nextInt(levels.size)),
            getRandomString(8),
            getRandomString(12),
            getRandomString(10),
            rating,
            getRandomString(25))
    }
}