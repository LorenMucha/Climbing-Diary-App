package com.main.climbingdiary.controller;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.models.SportType;

public class NavDrawerController implements NavigationView.OnNavigationItemSelectedListener {

    private final DrawerLayout drawer;


    public NavDrawerController(AppCompatActivity activity) {
        int layoutId = R.id.nav_drawer_layout;
        drawer = activity.findViewById(layoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, (Toolbar) activity.findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SportType type = SportType.stringToSportType(item.getTitle().toString());
        // Handle navigation view item clicks here.
        AppPreferenceManager.setSportType(type);
        FragmentPager.getInstance().initializeSportType();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
