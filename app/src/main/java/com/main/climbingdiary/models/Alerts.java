package com.main.climbingdiary.models;

import android.content.Context;

import com.main.climbingdiary.MainActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Alerts {
    public static void setErrorAlert(Context context){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Es ist ein Fehler aufgetreten.")
                .show();
    }
}
