package com.main.climbingdiary.activities

import android.R
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.main.climbingdiary.common.AppPermissions.checkPermissions
import com.main.climbingdiary.fragments.SettingsFragment
import java.util.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        lateinit var mInstance: SettingsActivity

        @JvmStatic
        fun getSettingsActivity(): Context {
            return mInstance.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInstance = this
        fragmentManager.beginTransaction().replace(R.id.content, SettingsFragment).commit()
        Objects.requireNonNull(supportActionBar)!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        checkPermissions(this.applicationContext)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}