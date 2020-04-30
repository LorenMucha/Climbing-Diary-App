package com.main.climbingdiary.controller;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.main.climbingdiary.R;
import com.main.climbingdiary.adapter.TabAdapter;
import com.main.climbingdiary.controller.button.AppFloatingActionButton;
import com.main.climbingdiary.controller.button.ShowTimeSlider;
import com.main.climbingdiary.fragments.MapFragment;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.fragments.StatisticFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentPager implements TabLayout.OnTabSelectedListener {
    private static final int view_layout = R.id.viewPager;
    private static final int tab_layout = R.id.tabLayout;
    @SuppressLint("StaticFieldLeak")
    private static TabLayout tabLayout;
    private static String tabTitle;
    public static String getTabTitle(){
        return tabTitle;
    }
    private static List<Tabs> fragmentMap = new ArrayList<>();

    static {
        fragmentMap.add(Tabs.STATISTIK);
        fragmentMap.add(Tabs.ROUTEN);
        fragmentMap.add(Tabs.PROJEKTE);
        fragmentMap.add(Tabs.MAP);
    }

    public FragmentPager(AppCompatActivity _activity){
        ViewPager viewPager = _activity.findViewById(view_layout);
        tabLayout = _activity.findViewById(tab_layout);
        TabAdapter adapter = new TabAdapter(_activity.getSupportFragmentManager());
        for(Tabs tab:fragmentMap){
            adapter.addFragment((Fragment) tab.getFragment(),tab.getTitle());
        }
        //adapter.addFragment(Tabs.MAP.getFragment(),Tabs.MAP.getTitle());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabTitle = Objects.requireNonNull(tab.getText()).toString();
        switch(tabTitle) {
            //statistic
            case StatisticFragment.TITLE:
                ShowTimeSlider.show();
                AppFloatingActionButton.hide();
                break;
            //routes
            case RouteDoneFragment.TITLE:
                ShowTimeSlider.show();
                AppFloatingActionButton.show();
                break;
            case MapFragment.TITLE:
                AppFloatingActionButton.show();
                ShowTimeSlider.hide();
                break;
            //projects
            case RouteProjectFragment.TITLE:
                ShowTimeSlider.hide();
                AppFloatingActionButton.show();
                break;
            default :
                ShowTimeSlider.hide();
                break;
        }
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static void setPosition(int pos){
        Objects.requireNonNull(tabLayout.getTabAt(pos)).select();
    }

    public static void refreshActualFragment(){
        int tabPosition = tabLayout.getSelectedTabPosition();
        RouteFragment fragment = fragmentMap.get(tabPosition).getFragment();
        fragment.refreshData();
    }

    public static void refreshAllFragments(){
        for(Tabs tab: fragmentMap){
            tab.getFragment().refreshData();
        }
    }
}
