package com.main.climbingdiary.controller


import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.SettingsActivity
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSportType
import com.main.climbingdiary.controller.FragmentPager.initializeSportType
import com.main.climbingdiary.error.SportTypeNotSupportedExeption
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
        when(val menuTitle = item.title){
            StringManager.getStringForId(R.string.nav_title_settings) -> switchToSettings()
            else->{
                switchSportType(menuTitle.toString())
            }
        }
        return true
    }

    private fun switchSportType(title:String){
        val type = stringToSportType(title)
        // Handle navigation view item clicks here.
        setSportType(type)
        initializeSportType()
        drawer!!.closeDrawer(GravityCompat.START)
    }

    private fun switchToSettings(){
        val intent = Intent(activity.applicationContext, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(activity.baseContext,intent,null)
    }
}