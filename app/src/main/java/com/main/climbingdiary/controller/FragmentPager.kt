package com.main.climbingdiary.controller

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.TabAdapter
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.button.AppFloatingActionButton
import com.main.climbingdiary.controller.button.ShowTimeSlider
import com.main.climbingdiary.fragments.RouteFragment
import com.main.climbingdiary.model.SportType
import com.main.climbingdiary.model.Tabs
import java.util.*

object FragmentPager : TabLayout.OnTabSelectedListener {

    private const val view_layout: Int = R.id.viewPager
    private const val tab_layout: Int = R.id.tabLayout
    private val tabLayout: TabLayout
    private val fragmentMap: Map<Tabs, RouteFragment> = LinkedHashMap<Tabs, RouteFragment>()

    init {
        val activity = MainActivity.getApplication()
        val viewPager: ViewPager = activity!!.findViewById(view_layout)
        tabLayout = activity.findViewById(tab_layout)
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        val adapter = TabAdapter(fragmentManager)
        //init the fragments
        //init the fragments
        for ((key, value) in fragmentMap) {
            adapter.addFragment(value as Fragment, key.toString())
        }

        //adapter.addFragment(Tabs.MAP.getFragment(),Tabs.MAP.getTitle());

        //adapter.addFragment(Tabs.MAP.getFragment(),Tabs.MAP.getTitle());
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val tabSelected: Tabs = Tabs.valueOf(tab.text.toString().toLowerCase(Locale.ROOT))
        AppPreferenceManager.setSelectedTabsTitle(tabSelected)
        when (tabSelected) {
            Tabs.STATISTIK -> {
                ShowTimeSlider.hide()
                AppFloatingActionButton.hide()
                AppPreferenceManager.setFilter("")
            }
            Tabs.ROUTEN, Tabs.BOULDER -> {
                ShowTimeSlider.show()
                AppFloatingActionButton.show()
                TimeSlider.setTimes()
            }
            Tabs.MAP -> {
                AppFloatingActionButton.show()
                ShowTimeSlider.hide()
            }
            Tabs.PROJEKTE -> {
                ShowTimeSlider.hide()
                AppFloatingActionButton.show()
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    fun setPosition(pos: Int) {
        Objects.requireNonNull(tabLayout.getTabAt(pos))!!.select()
    }

    fun refreshSelectedFragment() {
        refreshFragment(fragmentMap[AppPreferenceManager.getSelectedTabsTitle()])
    }

    fun refreshAllFragments() {
        for ((_, value) in fragmentMap) {
            refreshFragment(value)
        }
    }

    fun initializeSportType() {
        val type: SportType = AppPreferenceManager.getSportType()
        refreshAllFragments()
        tabLayout.getTabAt(1)!!.text = type.name
    }

    private fun refreshFragment(frg: RouteFragment?) {
        try {
            frg!!.refreshData()
        } catch (ex: Exception) {}
    }
}