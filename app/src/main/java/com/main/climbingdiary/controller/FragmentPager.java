package com.main.climbingdiary.controller;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.adapter.TabAdapter;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.button.AppFloatingActionButton;
import com.main.climbingdiary.controller.button.ShowTimeSlider;
import com.main.climbingdiary.fragments.MapFragment;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.fragments.StatisticFragment;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.models.Tabs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentPager implements TabLayout.OnTabSelectedListener {

    private static final int view_layout = R.id.viewPager;
    private static final int tab_layout = R.id.tabLayout;
    private final TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    private static FragmentPager INSTANZE = null;
    private static final Map<Tabs, RouteFragment> fragmentMap = new LinkedHashMap<>();

    static {
        fragmentMap.put(Tabs.STATISTIK, StatisticFragment.getInstance());
        fragmentMap.put(Tabs.ROUTEN, RouteDoneFragment.getInstance());
        fragmentMap.put(Tabs.PROJEKTE, RouteProjectFragment.getInstance());
    }


    private FragmentPager() {
        AppCompatActivity activity = MainActivity.getMainActivity();
        ViewPager viewPager = activity.findViewById(view_layout);
        tabLayout = activity.findViewById(tab_layout);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        TabAdapter adapter = new TabAdapter(fragmentManager);
        //init the fragments
        for(Map.Entry<Tabs,RouteFragment> entry : fragmentMap.entrySet()){
            adapter.addFragment((Fragment) entry.getValue(), entry.getKey().typeToString());
        }

        //adapter.addFragment(Tabs.MAP.getFragment(),Tabs.MAP.getTitle());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    public static FragmentPager getInstance(){
        if(INSTANZE==null){
            INSTANZE = new FragmentPager();
        }
        return INSTANZE;
    }

    public String getContextSport(){
        return tabLayout.getTabAt(1).getText().toString();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Tabs tabSelected = Tabs.stringToTabs(Objects.requireNonNull(tab.getText()).toString());
        AppPreferenceManager.setSelectedTabsTitle(tabSelected);
        Log.d("Selected Tab:",tabSelected.typeToString());
        switch (tabSelected) {
            //statistic
            case STATISTIK:
                ShowTimeSlider.hide();
                AppFloatingActionButton.hide();
                AppPreferenceManager.setFilter("");
                break;
            //routes
            case ROUTEN :
            case BOULDER:
                ShowTimeSlider.show();
                AppFloatingActionButton.show();
                TimeSlider.getInstance().setTimes();
                break;
            case MAP:
                AppFloatingActionButton.show();
                ShowTimeSlider.hide();
                break;
            //projects
            case PROJEKTE:
                ShowTimeSlider.hide();
                AppFloatingActionButton.show();
                break;
            default:
                ShowTimeSlider.hide();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    public void setPosition(int pos) {
        Objects.requireNonNull(tabLayout.getTabAt(pos)).select();
    }

    public void refreshSelectedFragment() {
        refreshFragment(fragmentMap.get(AppPreferenceManager.getSelectedTabsTitle()));
    }

    public void refreshAllFragments() {
        for(Map.Entry<Tabs,RouteFragment> entry : fragmentMap.entrySet()){
            refreshFragment(entry.getValue());
        }
    }

    public void initializeSportType(){
                SportType type = AppPreferenceManager.getSportType();
        refreshAllFragments();
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(type.getRouteName());
    }

    private void refreshFragment(RouteFragment frg){
        try{
            frg.refreshData();
        }catch (Exception ex){
            Log.d("refreshFragment:",ex.getLocalizedMessage());
        }
    }
}
