package com.main.climbingdiary.Ui.button;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.dialog.DialogFactory;

//ToDo
public class AddRoute implements View.OnClickListener {

    public static FloatingActionButton addRoute;

    public AddRoute(Activity _activity){
        addRoute = _activity.findViewById(R.id.addRoute);
        addRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogFactory.openAddRouteDialog(FragmentPager.getTabTitle());
    }

    public static void show(){
        Log.d("AddRoute","show");
        addRoute.show();
    }
    public static void hide(){
        Log.d("AddRoute","hide");
        addRoute.hide();
    }
}