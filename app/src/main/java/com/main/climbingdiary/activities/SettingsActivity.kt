package com.main.climbingdiary.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AppPermissions.checkPermissions
import com.main.climbingdiary.fragments.SettingsFragment
import java.util.*

class SettingsActivity : AppCompatActivity() {

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        fragmentManager.beginTransaction().replace(
            R.id.content,
            SettingsFragment
        ).commit()
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mainActivity: AppCompatActivity? = null
        val settingsActivity: AppCompatActivity?
            get() = mainActivity
    }
}