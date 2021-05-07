package com.main.climbingdiary.controller

import android.app.SearchManager
import android.content.Context
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.model.MenuValues
import com.main.climbingdiary.model.Tabs
import java.util.*

class AppBarMenu(val menu:Menu) : SearchView.OnQueryTextListener {

    private val searchId = R.id.action_search
    private val sortId = R.id.action_sort
    private val filterId = R.id.action_filter
    private var searchManager: SearchManager = MainActivity.CONTEXT?.getSystemService(
        Context.SEARCH_SERVICE) as SearchManager
    private var idList = listOf(searchId,sortId,filterId)

    init{
        val searchview = menu.findItem(searchId).actionView as SearchView
        searchview.setSearchableInfo(
            searchManager
                .getSearchableInfo(MainActivity.getComponentName())
        )
        searchview.maxWidth = Int.MAX_VALUE
        searchview.setOnQueryTextListener(this@AppBarMenu)
        // Fill the Menu for Filtering
        var i = 2
        for (areaName in AreaRepository.getAreaNameList()!!) {
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
        if (AppPreferenceManager.getSelectedTabsTitle() === Tabs.PROJEKTE) {
            RouteProjectFragment.getAdapter().getFilter().filter(query)
        } else {
            RouteDoneFragment.getAdapter().getFilter().filter(query)
        }
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.d("Query", query)
        // filter recycler view when text is changed
        if (AppPreferenceManager.getSelectedTabsTitle() === Tabs.PROJEKTE) {
            RouteProjectFragment.getAdapter().getFilter().filter(query)
        } else {
            RouteDoneFragment.getAdapter().getFilter().filter(query)
        }
        return false
    }

    fun setItemVisebility(item: MenuValues, state: Boolean) {
        val dateId = R.id.sort_date
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