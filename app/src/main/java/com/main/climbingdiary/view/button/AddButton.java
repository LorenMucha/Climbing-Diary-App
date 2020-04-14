package com.main.climbingdiary.view.button;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.view.FragmentPager;
import com.main.climbingdiary.view.dialog.DialogFactory;


public class AddButton implements View.OnClickListener {

    private static FloatingActionButton floatingActionButton;

    public AddButton(){
        floatingActionButton = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn);
        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setImageResource(R.drawable.ic_plus);
        floatingActionButton.show();
    }

    @Override
    public void onClick(View view) {
        DialogFactory.openAddRouteDialog(FragmentPager.getTabTitle());
    }
    public static void show() {
        floatingActionButton.show();
    }

    public static void hide() {
        floatingActionButton.hide();
    }
}
