package com.main.climbingdiary.controller.button;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.dialog.DialogFactory;
import com.main.climbingdiary.models.Tabs;

public class AppFloatingActionButton implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static FloatingActionButton floatingActionButtonAdd;
    @SuppressLint("StaticFieldLeak")
    private static FloatingActionButton floatingActionButtonLocate;

    public AppFloatingActionButton() {
        floatingActionButtonAdd = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn_add);
        floatingActionButtonLocate = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn_locate);
        floatingActionButtonAdd.setOnClickListener(this);
        floatingActionButtonLocate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        DialogFactory.openAddRouteDialog(AppPreferenceManager.getSelectedTabsTitle());
    }

    public static void show() {
        hide();
        if (AppPreferenceManager.getSelectedTabsTitle() == Tabs.MAP) {
            floatingActionButtonLocate.show();
        } else {
            floatingActionButtonAdd.show();
        }
    }

    public static void hide() {
        floatingActionButtonAdd.hide();
        floatingActionButtonLocate.hide();
    }
}
