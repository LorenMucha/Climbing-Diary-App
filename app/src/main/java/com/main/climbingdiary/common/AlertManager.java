package com.main.climbingdiary.common;

import android.content.Context;

import com.main.climbingdiary.models.Alert;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AlertManager {
    public static void setErrorAlert(Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Es ist ein Fehler aufgetreten.")
                .show();
    }

    public void setAlertWithoutContent(Context context, Alert alert) {
        new SweetAlertDialog(context, alert.getDialogType())
                .setTitleText(alert.getTitle())
                .show();
    }
}


