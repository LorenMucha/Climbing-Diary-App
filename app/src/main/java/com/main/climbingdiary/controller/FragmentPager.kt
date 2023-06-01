package com.main.climbingdiary.controller

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.TabAdapter
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSelectedTabsTitle
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSportType
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSelectedTabsTitle
import com.main.climbingdiary.controller.button.AppFloatingActionButton
import com.main.climbingdiary.controller.button.ShowTimeSlider
import com.main.climbingdiary.controller.slider.TimeSliderFactory
import com.main.climbingdiary.error.TabsNotSupportedException
import com.main.climbingdiary.fragments.RouteDoneFragment
import com.main.climbingdiary.fragments.RouteFragment
import com.main.climbingdiary.fragments.RouteProjectFragment
import com.main.climbingdiary.fragments.StatisticFragment
import com.main.climbingdiary.models.Tabs
import com.main.climbingdiary.models.Tabs.Companion.stringToTabs
import java.util.Objects

object FragmentPager : TabLayout.OnTabSelectedListener {

    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private const val viewLayout: Int = R.id.viewPager
    private const val layoutTabs: Int = R.id.tabLayout
    private val fragmentMap: Map<Tabs, RouteFragment> = mapOf(
        Tabs.STATISTIK to StatisticFragment(),
        Tabs.ROUTEN to RouteDoneFragment,
        Tabs.PROJEKTE to RouteProjectFragment
    )
    private var tabLayout: TabLayout = activity.findViewById(layoutTabs)

    fun createViewPager() {
        val viewPager = activity.findViewById<ViewPager>(viewLayout)
        tabLayout = activity.findViewById(layoutTabs)
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        val adapter = TabAdapter(fragmentManager)
        //init the fragments
        for ((key, value) in fragmentMap) {
            key.typeToString().let { adapter.addFragment(value as Fragment, it) }
        }
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        try {
            val tabSelected =
                stringToTabs(Objects.requireNonNull(tab.text).toString())
            setSelectedTabsTitle(tabSelected)
            when (tabSelected) {
                Tabs.STATISTIK -> {
                    ShowTimeSlider.hide()
                    AppFloatingActionButton.hide()
                    setFilter("")
                }
                Tabs.ROUTEN, Tabs.BOULDER -> {
                    ShowTimeSlider.show()
                    AppFloatingActionButton.show()
                    TimeSliderFactory.setSlider()
                }
                Tabs.PROJEKTE -> {
                    ShowTimeSlider.hide()
                    AppFloatingActionButton.show()
                }
            }
        }catch(e: TabsNotSupportedException){
            AlertFactory.getErrorAlert(context = activity.baseContext)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}

    fun setPosition(pos: Int) {
        tabLayout.getTabAt(pos)!!.select()
    }

    fun refreshSelectedFragment() {
        refreshFragment(fragmentMap[getSelectedTabsTitle()])
    }

    fun refreshAllFragments() {
        for ((_, value) in fragmentMap) {
            refreshFragment(value)
        }
    }

    fun initializeSportType() {
        val type = getSportType()
        refreshAllFragments()
        tabLayout.getTabAt(1)!!.text = type.getRouteName()
    }

    private fun refreshFragment(frg: RouteFragment?) {
        try {
            frg!!.refreshData()
        } catch (ex: Exception) {
            Log.d("refreshFragment:", ex.localizedMessage!!)
        }
    }
}