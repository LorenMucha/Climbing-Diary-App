package com.main.climbingdiary.view;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.main.climbingdiary.R;

public class NavDrawer implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    public NavDrawer(AppCompatActivity _activity){
        int layoutId = R.id.drawer_layout;
        drawer = _activity.findViewById(layoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                _activity, drawer, (Toolbar) _activity.findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = _activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
