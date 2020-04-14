package com.main.climbingdiary.view.button;

import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.fragments.MapFragment;

public class ShowLocationButton implements View.OnClickListener{

    private static FloatingActionButton floatingActionButton;

    public ShowLocationButton(){
        floatingActionButton = MainActivity.getMainActivity().findViewById(R.id.floating_action_btn);
        floatingActionButton.setOnClickListener(this);
        floatingActionButton.setImageResource(R.drawable.ic_locate);
        show();
    }

    @Override
    public void onClick(View v) {
        MapFragment.setUserPosition();
    }

    public static void show() {
        floatingActionButton.show();
    }

    public static void hide() {
        floatingActionButton.hide();
    }
}
