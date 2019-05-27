package com.main.climbingdiary.Ui.button;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.dialog.DialogFactory;

//ToDo
public class AddRoute implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static FloatingActionButton addRoute;

    public AddRoute(Activity _activity){
        addRoute = _activity.findViewById(R.id.addRoute);
        addRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogFactory.openAddRouteDialog(FragmentPager.getTabTitle());
    }

    public static void show(){
        addRoute.show();
    }
    public static void hide(){
        addRoute.hide();
    }
}
