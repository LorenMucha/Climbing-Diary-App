package com.main.climbingdiary.common.preferences

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.model.Alert

object AlertManager {
    fun setErrorAlert(context: Context?) {
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText("Es ist ein Fehler aufgetreten.")
            .show()
    }

    fun setAlertWithoutContent(context: Context?, alert: Alert) {
        SweetAlertDialog(context, alert.dialogType)
            .setTitleText(alert.title)
            .show()
    }
}