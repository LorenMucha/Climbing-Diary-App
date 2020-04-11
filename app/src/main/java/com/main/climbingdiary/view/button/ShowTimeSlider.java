package com.main.climbingdiary.view.button;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.main.climbingdiary.R;

public class ShowTimeSlider implements View.OnClickListener{

    @SuppressLint("StaticFieldLeak")
    private static ImageButton imageButton;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout layout;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout container;
    private int clickSet =0;

    public ShowTimeSlider(Activity _activity){
        imageButton = _activity.findViewById(R.id.showTimeSlider);
        imageButton.setOnClickListener(this);
        layout = _activity.findViewById(R.id.showTimeLayout);
        container = _activity.findViewById(R.id.sliderLayout);
    }

    @Override
    public void onClick(View view) {
        if(clickSet==0) {
            layout.setVisibility(View.VISIBLE);
            imageButton.setImageResource(android.R.drawable.arrow_down_float);
            container.setZ(1000);
            clickSet++;
        }else{
            layout.setVisibility(View.GONE);
            imageButton.setImageResource(android.R.drawable.arrow_up_float);
            clickSet=0;
        }
    }
    public static void show(){
        container.setVisibility(View.VISIBLE);
    }
    public static void hide(){
        container.setVisibility(View.GONE);
    }
    public static void hideButton(){imageButton.setVisibility(View.GONE);}
    public static void showButton(){imageButton.setVisibility(View.VISIBLE);}
}
