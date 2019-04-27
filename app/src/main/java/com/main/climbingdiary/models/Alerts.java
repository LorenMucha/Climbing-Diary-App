package com.main.climbingdiary.models;

import com.main.climbingdiary.MainActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Alerts {
    public static void setErrorAlert(){
        new SweetAlertDialog(MainActivity.getAppContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Es ist ein Fehler aufgetreten.")
                .show();
    }
}
