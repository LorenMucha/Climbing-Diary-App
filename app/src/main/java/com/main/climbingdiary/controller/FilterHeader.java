package com.main.climbingdiary.controller;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteFragment;
import com.main.climbingdiary.models.Filter;

public class FilterHeader implements View.OnClickListener {

    private LinearLayout LAYOUT;
    private TextView TEXT;
    private ImageView IMAGE;
    private String filterText;
    private RouteFragment fragment;

    public FilterHeader(RouteFragment _fragment) {
        View view = _fragment.getView();
        assert view != null;
        LAYOUT = view.findViewById(R.id.filter_header);
        TEXT = view.findViewById(R.id.filter_header_txt);
        IMAGE = view.findViewById(R.id.filter_image);
        LAYOUT.setOnClickListener(this);
        fragment = _fragment;
    }

    public void show(){
        show(filterText);
    }

    public void show(String value){
        LAYOUT.setVisibility(View.VISIBLE);
        filterText = value;
        TEXT.setText(filterText);
        Filter.setFilter(String.format("g.name like '%s'",filterText.toLowerCase()), MenuValues.FILTER);
        IMAGE.setImageResource(R.drawable.ic_filter_active);
        fragment.refreshData();
    }
    public void hide(){
        LAYOUT.setVisibility(View.GONE);
        if(Filter.getSetter()== MenuValues.FILTER){
            Filter.removeFilter();
        }
        IMAGE.setImageResource(R.drawable.ic_filter);
        filterText = "";
        fragment.refreshData();
    }

    @Override
    public void onClick(View v) {
        hide();
    }
}
