package com.main.climbingdiary.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.main.climbingdiary.R
import com.main.climbingdiary.common.LanguageManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.preferences.AppPreferenceManager.removeAllFilterPrefs
import com.main.climbingdiary.controller.FragmentPager.createViewPager
import com.main.climbingdiary.controller.NavDrawerController
import com.main.climbingdiary.showcase.ShowCaseProvider

class MainActivity : AppCompatActivity() {

    private val layoutId: Int = R.layout.activity_main

    companion object {
        lateinit var mInstance: MainActivity

        @Synchronized
        fun getMainAppContext(): Context {
            return mInstance.applicationContext
        }

        @Synchronized
        fun getMainActivity(): AppCompatActivity {
            return mInstance
        }

        @Synchronized
        fun getMainComponentName(): ComponentName? {
            return mInstance.componentName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInstance = this
        //must be before set content view to get the right view
        LanguageManager(this).setLanguage()
        setContentView(layoutId)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        removeAllFilterPrefs()
        createViewPager()
        //navigation View
        NavDrawerController(this)
        if(!AppPreferenceManager.getLanguageFirstTime()) {
            ShowCaseProvider(this).createShowCase()
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.nav_drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.app_settings) {
            val intent = Intent(this.applicationContext, SettingsActivity::class.java)
            startActivity(intent)
        }
        return false
    }
}