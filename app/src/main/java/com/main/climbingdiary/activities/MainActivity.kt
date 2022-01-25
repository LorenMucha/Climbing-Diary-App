package com.main.climbingdiary.activities

import android.annotation.SuppressLint
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
import com.erkutaras.showcaseview.ShowcaseManager
import com.main.climbingdiary.R
import com.main.climbingdiary.common.parser.EightAparser
import com.main.climbingdiary.common.preferences.AppPreferenceManager.removeAllFilterPrefs
import com.main.climbingdiary.controller.FragmentPager.createViewPager
import com.main.climbingdiary.controller.NavDrawerController


@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    private val layoutId: Int = R.layout.activity_main

    companion object {
        lateinit var mInstance: MainActivity

        fun getMainAppContext(): Context {
            return mInstance.applicationContext
        }

        fun getMainActivity(): AppCompatActivity {
            return mInstance
        }

        fun getMainComponentName(): ComponentName? {
            return mInstance.componentName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        mInstance = this

        removeAllFilterPrefs()
        createViewPager()
        //navigation View
        NavDrawerController(this)
        EightAparser(this).login()
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