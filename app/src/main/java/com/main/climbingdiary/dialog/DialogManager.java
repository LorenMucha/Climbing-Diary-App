package com.main.climbingdiary.dialog;

import android.support.v7.app.AppCompatActivity;

import com.main.climbingdiary.MainActivity;

public class DialogManager {
    public static void openAddRouteDialog(){
        AddRouteDialog addRoute = AddRouteDialog.newInstance("Neue Route");
        addRoute.show(MainActivity.getMainActivity().getSupportFragmentManager(),"fragment_add_Route");
    }
    public static void openEditRouteDialog(int _id){
        EditRouteDialog editDialog = EditRouteDialog.newInstance("Route bearbeiten",_id);
        editDialog.show(MainActivity.getMainActivity().getSupportFragmentManager(),"edit");
    }
}
