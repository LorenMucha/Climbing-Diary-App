package com.main.climbingdiary.controller

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.model.SportType
import java.util.*

class NavDrawerController(activity: AppCompatActivity) : NavigationView.OnNavigationItemSelectedListener {

    private val drawer: DrawerLayout

    init{
        val layoutId: Int = R.id.nav_drawer_layout
        drawer = activity.findViewById(layoutId)
        val toggle = ActionBarDrawerToggle(
            activity,
            drawer,
            activity.findViewById(R.id.toolbar) as Toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = activity.findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this@NavDrawerController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val type: SportType = SportType.valueOf(item.title.toString().toUpperCase(Locale.ROOT))
        // Handle navigation view item clicks here.
        AppPreferenceManager.setSportType(type)
        FragmentPager.initializeSportType()
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}