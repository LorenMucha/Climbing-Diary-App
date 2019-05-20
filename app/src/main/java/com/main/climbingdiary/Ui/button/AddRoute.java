package com.main.climbingdiary.Ui.button;

import android.view.View;

import com.main.climbingdiary.R;
import com.main.climbingdiary.dialog.DialogManager;

public class AddRoute implements View.OnClickListener {
    private int layout_id = R.id.addRoute;

    @Override
    public void onClick(View view) {
        DialogManager.openAddRouteDialog(view.getContext());
    }
}
