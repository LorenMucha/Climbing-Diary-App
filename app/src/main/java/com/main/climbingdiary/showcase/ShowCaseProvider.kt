package com.main.climbingdiary.showcase

import android.content.Context
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.erkutaras.showcaseview.ShowcaseManager
import com.main.climbingdiary.R
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.StringProvider
import com.main.climbingdiary.common.StringProvider.getString
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.models.Alert

class ShowCaseProvider(private val context: Context) {

    fun createShowCase() {
        AppPreferenceManager
            .isUsedFirstTime()
            .run {
                if (!this) {
                    setAlert()
                }
            }.also {
                AppPreferenceManager.setIsUsedFirstTime(true)
            }
    }

    private fun createBuilder(view: View): ShowcaseManager.Builder {
        return ShowcaseManager.Builder().context(context)
            .view(view)
            .descriptionImageRes(R.mipmap.ic_launcher_round)
            .descriptionTitle(getString(R.string.showcase_header))
            .descriptionText(getString(R.string.showcase_info))
            .buttonText("DONE")
            .key("Welcome")
            .developerMode(true)
            .marginFocusArea(0)
            .gradientFocusEnabled(true)
            .add()
    }

    private fun setAlert(){
        val alert = AlertFactory.getAlert(
            context, Alert(
                title = getString(R.string.app_welcome),
                message = getString(R.string.question_text_show_case),
                dialogType = SweetAlertDialog.CUSTOM_IMAGE_TYPE
            )
        )
        alert.setCustomImage(R.drawable.waving_hand)
            .setConfirmText("Gerne")
            .setConfirmClickListener {
                it.let {
                    val showcase = createBuilder(it.window!!.decorView).build()
                    showcase.show()
                    it.cancel()
                }
            }
            .setCancelButton("Nein", { it.cancel() })
            .show()
    }
}