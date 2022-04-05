package com.main.climbingdiary.common

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.models.Alert

object AlertFactory {

    fun getAlert(context: Context, alert: Alert):SweetAlertDialog {
        val dialog = SweetAlertDialog(context, alert.dialogType!!)

        alert.image?.let {
            dialog.setCustomImage(it)
        }
        alert.title?.let {
            dialog.setTitle(it)
        }
        alert.message?.let {
            dialog.setContentText(it)
        }
        return dialog
    }

    fun getErrorAlert(context: Context):SweetAlertDialog {
          return SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
              .setTitleText("Oops...")
              .setContentText(context.getString(R.string.alert_error_text))
      }
}