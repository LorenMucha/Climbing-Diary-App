package com.main.climbingdiary.view;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.main.climbingdiary.R;
import com.main.climbingdiary.fragments.MapFragment;
import com.main.climbingdiary.view.button.AddButton;
import com.main.climbingdiary.view.button.ShowLocationButton;
import com.main.climbingdiary.view.button.ShowTimeSlider;
import com.main.climbingdiary.adapter.TabAdapter;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.fragments.StatisticFragment;

import java.util.Objects;

public class FragmentPager implements TabLayout.OnTabSelectedListener {
    private static final int view_layout = R.id.viewPager;
    private static final int tab_layout = R.id.tabLayout;
    private ViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    private static TabLayout tabLayout;
    private TabAdapter adapter;
    private static String tabTitle;

    public static String getTabTitle(){
        return tabTitle;
    }

    public FragmentPager(AppCompatActivity _activity){
        viewPager = _activity.findViewById(view_layout);
        tabLayout = _activity.findViewById(tab_layout);
        adapter = new TabAdapter(_activity.getSupportFragmentManager());
    }

    public void setFragmente(){
        //create the necessary Fragments
        adapter.addFragment(Tabs.STATISTIK.getFragment(),Tabs.STATISTIK.getTitle());
        adapter.addFragment(Tabs.ROUTEN.getFragment(),Tabs.ROUTEN.getTitle());
        adapter.addFragment(Tabs.PROJEKTE.getFragment(),Tabs.PROJEKTE.getTitle());
        adapter.addFragment(Tabs.MAP.getFragment(),Tabs.MAP.getTitle());
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
                StatisticFragment.setFilterMenu();
                ShowTimeSlider.show();
                break;
            //routes
            case RouteDoneFragment.TITLE:
                RouteDoneFragment.setFilterMenu();
                ShowTimeSlider.show();
                new AddButton();
                break;
            case MapFragment.TITLE:
                new ShowLocationButton();
                ShowTimeSlider.hide();
                break;
            //projects
            case RouteProjectFragment.TITLE:
                RouteProjectFragment.setFilterMenu();
                ShowTimeSlider.hide();
                new AddButton();
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
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        Objects.requireNonNull(tab).select();
    }
    public static void refreshAllFragments(){
        RouteProjectFragment.refreshData();
        RouteDoneFragment.refreshData();
        StatisticFragment.refreshData();
    }
}
