package com.main.climbingdiary.showcase

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.erkutaras.showcaseview.ShowcaseManager
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.models.Alert

object ShowCaseProvider {

    fun createShowCase(context:Context) {
        val alert = AlertFactory.getAlert(
            context, Alert(
                title = "Wilkommen",
                message = "Möchtest du eine kleine Info Tour durch die App unternehmen ?",
                dialogType = SweetAlertDialog.CUSTOM_IMAGE_TYPE
            )
        )
        alert.setCustomImage(R.drawable.waving_hand)
            .setConfirmClickListener {
                it.run {
                    val builder = ShowcaseManager.Builder()
                    builder.context(context)
                        .view(it.window!!.decorView)
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
            .show()
    }
}