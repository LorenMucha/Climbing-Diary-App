package com.main.climbingdiary.common

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.models.Alert

object AlertManager {

    @JvmStatic
    fun setErrorAlert(context:Context){
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Oops...")
            .setContentText("Es ist ein Fehler aufgetreten.")
            .show()
    }
    @JvmStatic
    fun setAlertWithoutContent(context: Context, alert:Alert){
        SweetAlertDialog(context, alert.dialogType!!)
            .setTitleText(alert.title)
            .show()
    }
}