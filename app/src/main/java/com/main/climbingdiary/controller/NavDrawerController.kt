package com.main.climbingdiary.controller


import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.main.climbingdiary.R
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSportType
import com.main.climbingdiary.controller.FragmentPager.initializeSportType
import com.main.climbingdiary.models.SportType.Companion.stringToSportType

class NavDrawerController(val activity: AppCompatActivity) : NavigationView.OnNavigationItemSelectedListener {
    private val layoutId: Int = R.id.nav_drawer_layout
    private var drawer: DrawerLayout? = activity.findViewById(layoutId)

   init {
        val toggle = ActionBarDrawerToggle(
            activity,
            drawer,
            activity.findViewById<View>(R.id.toolbar) as Toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = activity.findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val type = stringToSportType(item.title.toString())
        // Handle navigation view item clicks here.
        setSportType(type)
        initializeSportType()
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }
}