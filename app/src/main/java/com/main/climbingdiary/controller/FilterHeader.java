package com.main.climbingdiary.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.fragments.RouteFragment;
import com.main.climbingdiary.models.MenuValues;

public class FilterHeader implements View.OnClickListener {

    private final LinearLayout LAYOUT;
    private final TextView TEXT;
    private final ImageView IMAGE;
    private final RouteFragment fragment;
    private String filterText;

    public FilterHeader(RouteFragment _fragment) {
        View view = _fragment.getView();
        assert view != null;
        LAYOUT = view.findViewById(R.id.filter_header);
        TEXT = view.findViewById(R.id.filter_header_txt);
        IMAGE = view.findViewById(R.id.filter_image);
        LAYOUT.setOnClickListener(this);
        fragment = _fragment;
    }

    public void show() {
        show(filterText);
    }

    public void show(String value) {
        LAYOUT.setVisibility(View.VISIBLE);
        filterText = value;
        TEXT.setText(filterText);
        AppPreferenceManager.setFilter(String.format("g.name like '%s'", filterText.toLowerCase()));
        AppPreferenceManager.setFilterSetter(MenuValues.FILTER);
        IMAGE.setImageResource(R.drawable.ic_filter_active);
        fragment.refreshData();
    }

    public void hide() {
        LAYOUT.setVisibility(View.GONE);
        if (AppPreferenceManager.getFilterSetter() == MenuValues.FILTER) {
            AppPreferenceManager.removeAllFilterPrefs();
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
