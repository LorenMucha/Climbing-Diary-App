package com.example.climbingdiary.models;

import com.example.climbingdiary.MainActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Alerts {
    public static void setErrorAlert(){
        new SweetAlertDialog(MainActivity.getAppContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Es ist ein Fehler aufgetreten.")
                .show();
    }
}
