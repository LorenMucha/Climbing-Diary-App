package com.main.climbingdiary.Ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.main.climbingdiary.R;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.adapter.TabAdapter;
import com.main.climbingdiary.abstraction.Tabs;

public class FragmentPager implements TabLayout.OnTabSelectedListener {
    private static final int view_layout = R.id.viewPager;
    private static final int tab_layout = R.id.tabLayout;
    private ViewPager viewPager;
    private static TabLayout tabLayout;
    private TabAdapter adapter;
    private FragmentManager fm;
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
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabTitle = tab.getText().toString();
        switch(tab.getPosition()) {
            case 0:
                AddRoute.hide();
                break;
            default :
                AddRoute.show();
                break;
        }
    }

    public static void setPosition(int pos){
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
