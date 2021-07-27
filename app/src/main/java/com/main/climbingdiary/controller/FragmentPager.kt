package com.main.climbingdiary.controller

import android.annotation.SuppressLint
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.adapter.TabAdapter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSelectedTabsTitle
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSportType
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setFilter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSelectedTabsTitle
import com.main.climbingdiary.controller.button.AppFloatingActionButton
import com.main.climbingdiary.controller.button.ShowTimeSlider
import com.main.climbingdiary.fragments.RouteDoneFragment
import com.main.climbingdiary.fragments.RouteFragment
import com.main.climbingdiary.fragments.RouteProjectFragment
import com.main.climbingdiary.fragments.StatisticFragment
import com.main.climbingdiary.models.Tabs
import com.main.climbingdiary.models.Tabs.Companion.stringToTabs
import java.util.*

@SuppressLint("StaticFieldLeak")
object FragmentPager: TabLayout.OnTabSelectedListener {

    private val activity: AppCompatActivity by lazy { MainActivity.getMainActivity() }
    private const val viewLayout: Int = R.id.viewPager
    private const val layoutTabs: Int = R.id.tabLayout
    private val fragmentMap: Map<Tabs, RouteFragment> = mapOf(
        Tabs.STATISTIK to StatisticFragment,
        Tabs.ROUTEN to RouteDoneFragment,
        Tabs.PROJEKTE to RouteProjectFragment
    )
    private var tabLayout: TabLayout = activity.findViewById(layoutTabs)

    fun create(){
        val viewPager = activity.findViewById<ViewPager>(viewLayout)
        tabLayout = activity.findViewById(layoutTabs)
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        val adapter = TabAdapter(fragmentManager)
        //init the fragments
        //init the fragments
        for ((key, value) in fragmentMap) {
            key.typeToString()?.let { adapter.addFragment(value as Fragment, it) }
        }
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val tabSelected =
            stringToTabs(Objects.requireNonNull(tab.text).toString())!!
        setSelectedTabsTitle(tabSelected)
        Log.d("Selected Tab:", tabSelected.typeToString())
        when (tabSelected) {
            Tabs.STATISTIK -> {
                ShowTimeSlider.hide()
                AppFloatingActionButton.hide()
                setFilter("")
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
        Objects.requireNonNull(tabLayout.getTabAt(1))!!.text = type.getRouteName()
    }

    private fun refreshFragment(frg: RouteFragment?) {
        try {
            frg!!.refreshData()
        } catch (ex: Exception) {
            Log.d("refreshFragment:", ex.localizedMessage)
        }
    }
}