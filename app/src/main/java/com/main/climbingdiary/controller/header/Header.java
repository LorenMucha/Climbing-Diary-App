package com.main.climbingdiary.controller.header;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public interface Header {
    static void setText(TextView textView, String text){
        textView.setText(text);
    }
    static void show(LinearLayout layout){
        layout.setVisibility(View.VISIBLE);
    }
    static void hide(LinearLayout layout){
        layout.setVisibility(View.GONE);
    }
}
