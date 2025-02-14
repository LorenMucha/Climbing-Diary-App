package com.main.climbingdiary.activities

//noinspection SuspiciousImport
import android.R
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.climbingdiary.common.AppPermissions
import com.main.climbingdiary.fragments.SettingsFragment

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
        supportFragmentManager.beginTransaction().replace(R.id.content, SettingsFragment()).commit()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Thread {
            this@SettingsActivity.runOnUiThread {
                AppPermissions.checkPermissions(this)
            }
        }.start()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}