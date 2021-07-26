package com.main.climbingdiary.controller

import android.app.SearchManager
import android.content.Context
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSelectedTabsTitle
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.fragments.RouteDoneFragment
import com.main.climbingdiary.fragments.RouteProjectFragment
import com.main.climbingdiary.models.MenuValues
import com.main.climbingdiary.models.Tabs
import java.util.*

class AppBarMenu(val menu: Menu) : SearchView.OnQueryTextListener {
    private val searchId: Int = R.id.action_search
    private val sortId: Int = R.id.action_sort
    private val filterId: Int = R.id.action_filter
    private val idList: MutableList<Int> = ArrayList()
    private val context: Context by lazy { MainActivity.getMainAppContext() }
    private val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager

    init {
        val searchview = menu.findItem(searchId).actionView as SearchView
        val repo = AreaRepository
        searchview.setSearchableInfo(
            searchManager
                .getSearchableInfo(MainActivity.getMainComponentName())
        )
        searchview.maxWidth = Int.MAX_VALUE
        searchview.setOnQueryTextListener(this)
        idList.add(searchId)
        idList.add(sortId)
        idList.add(filterId)
        // Fill the Menu for Filtering
        var i = 2
        for (areaName in repo.getAreaNameList()) {
            menu.findItem(R.id.filter_area).subMenu.add(
                R.id.filter_area + 1,
                i + R.id.filter_area,
                Menu.NONE,
                areaName
            )
            i++
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // filter recycler view when query submitted
        if (getSelectedTabsTitle() === Tabs.PROJEKTE) {
            RouteProjectFragment.getAdapter().filter.filter(query)
        } else {
            RouteDoneFragment.getAdapter().filter.filter(query)
        }
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.d("Query", query)
        // filter recycler view when text is changed
        if (getSelectedTabsTitle() === Tabs.PROJEKTE) {
            RouteProjectFragment.getAdapter().filter.filter(query)
        } else {
            RouteDoneFragment.getAdapter().filter.filter(query)
        }
        return false
    }

    fun setItemVisebility(item: MenuValues, state: Boolean) {
        val dateId: Int = R.id.sort_date
        when (item.toString()) {
            "date" -> menu.findItem(dateId).isVisible = state
            "search" -> menu.findItem(searchId).isVisible = state
            "sort" -> menu.findItem(sortId).isVisible = state
            "filter" -> menu.findItem(filterId).isVisible = state
        }
    }

    //overloading to hide all elements
    fun setItemVisebility(state: Boolean) {
        for (id in idList) {
            menu.findItem(id).isVisible = state
        }
    }
}