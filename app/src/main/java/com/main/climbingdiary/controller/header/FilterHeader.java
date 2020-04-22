package com.main.climbingdiary.controller.header;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.models.Filter;
//Fixme: need to be refactored !
public class FilterHeader implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static LinearLayout LAYOUT;
    @SuppressLint("StaticFieldLeak")
    private static TextView TEXT;
    @SuppressLint("StaticFieldLeak")
    private static ImageView IMAGE;
    private static boolean isVisible = false;
    private static String filterText;
    @SuppressLint("StaticFieldLeak")
    private static View view;

    public FilterHeader(View _view) {
        view = _view;
        LAYOUT = view.findViewById(R.id.filter_header);
        TEXT = view.findViewById(R.id.filter_header_txt);
        IMAGE = view.findViewById(R.id.filter_image);
        initializeView();
        LAYOUT.setOnClickListener(this);
    }
    private void initializeView(){
        try{
            if(isVisible){
                show();
            }else{
                hide();
            }
        }catch(Exception ignored){}
    }
    public static void show(){
        show(filterText);
    }
    public static void show(String value){
        LAYOUT.setVisibility(View.VISIBLE);
        filterText = value;
        TEXT.setText(filterText);
        Filter.setFilter(String.format("g.name like '%s'",filterText.toLowerCase()), MenuValues.FILTER);
        IMAGE.setImageResource(R.drawable.ic_filter_active);
        FragmentPager.refreshAllFragments();
        isVisible = true;
    }
    public static void hide(){
        LAYOUT.setVisibility(View.GONE);
        if(Filter.getSetter()==MenuValues.FILTER){
            Filter.removeFilter();
        }
        IMAGE.setImageResource(R.drawable.ic_filter);
        FragmentPager.refreshAllFragments();
        isVisible = false;
        filterText = "";
    }

    @Override
    public void onClick(View v) {
        hide();
    }
}
