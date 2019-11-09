package com.main.climbingdiary.Ui.header;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.Ui.menu.MenuValues;
import com.main.climbingdiary.models.Filter;

public class FilterHeader implements Header {

    private LinearLayout layout;
    private TextView text;
    private ImageView image;
    private static boolean isVisible = false;
    private static String filterText;

    public FilterHeader(View view) {
        layout = view.findViewById(R.id.filter_header);
        text = view.findViewById(R.id.filter_header_txt);
        image = view.findViewById(R.id.filter_image);
        layout.setOnClickListener(v -> {
            this.hide();
        });
        initializeView();
    }
    private void initializeView(){
        try{
            if(isVisible){
                this.show();
            }else{
                this.hide();
            }
        }catch(Exception ex){}
    }
    public void show(){
        this.show(filterText);
    }
    public void show(String value){
        Header.show(layout);
        filterText = value;
        Header.setText(text,filterText);
        Filter.setFilter(String.format("g.name like '%s'",filterText.toLowerCase()), MenuValues.FILTER);
        image.setImageResource(R.drawable.ic_filter_active);
        FragmentPager.refreshAllFragments();;
        isVisible = true;
    }
    public void hide(){
        Header.hide(layout);
        if(Filter.getSetter()==MenuValues.FILTER){
            Filter.removeFilter();
        }
        image.setImageResource(R.drawable.ic_filter);
        FragmentPager.refreshAllFragments();
        isVisible = false;
        filterText = "";
    }
}
