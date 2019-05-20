package com.main.climbingdiary.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

//ToDo
public class DialogManager {
    public static void openAddRouteDialog(Context context){
        AppCompatActivity activity = (AppCompatActivity) context;
        AddRouteDialog addRoute = AddRouteDialog.newInstance("Neue Route");
        addRoute.show(activity.getSupportFragmentManager(),"fragment_add_Route");
    }
    public static void openEditRouteDialog(Context context,int _id){
        AppCompatActivity activity = (AppCompatActivity) context;
        EditRouteDialog editDialog = EditRouteDialog.newInstance("Route bearbeiten",_id);
        editDialog.show(activity.getSupportFragmentManager(),"edit");
    }
}
