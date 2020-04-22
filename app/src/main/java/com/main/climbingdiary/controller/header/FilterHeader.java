package com.main.climbingdiary.controller.header;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.models.Filter;
//Fixme: need to be refactored !
public class FilterHeader implements Header{

    @SuppressLint("StaticFieldLeak")
    private static LinearLayout LAYOUT;
    @SuppressLint("StaticFieldLeak")
    private static TextView TEXT;
    @SuppressLint("StaticFieldLeak")
    private static ImageView IMAGE;
    private static boolean isVisible = false;
    private static String filterText;

    public FilterHeader(View view) {
        initializeView();
        LAYOUT = view.findViewById(R.id.filter_header);
        TEXT = view.findViewById(R.id.filter_header_txt);
        IMAGE = view.findViewById(R.id.filter_image);
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
        Header.show(LAYOUT);
        filterText = value;
        Header.setText(TEXT,filterText);
        Filter.setFilter(String.format("g.name like '%s'",filterText.toLowerCase()), MenuValues.FILTER);
        IMAGE.setImageResource(R.drawable.ic_filter_active);
        FragmentPager.refreshAllFragments();;
        isVisible = true;
    }
    public static void hide(){
        Header.hide(LAYOUT);
        if(Filter.getSetter()==MenuValues.FILTER){
            Filter.removeFilter();
        }
        IMAGE.setImageResource(R.drawable.ic_filter);
        FragmentPager.refreshAllFragments();
        isVisible = false;
        filterText = "";
    }
}
