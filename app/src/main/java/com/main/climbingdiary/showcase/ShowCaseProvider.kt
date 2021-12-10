package com.main.climbingdiary.showcase

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.erkutaras.showcaseview.ShowcaseManager
import com.main.climbingdiary.R

object ShowCaseProvider {
    fun createShowCase(v: View, activity: AppCompatActivity){
        val builder = ShowcaseManager.Builder()
        builder.context(activity)
            .view(v)
            .descriptionImageRes(R.mipmap.ic_launcher_round)
            .descriptionTitle("LOREM IPSUM DOLOR")
            .descriptionText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")
            .buttonText("DONE")
            .key("TEST")
            .developerMode(true)
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add().build()
            .show()
    }
}