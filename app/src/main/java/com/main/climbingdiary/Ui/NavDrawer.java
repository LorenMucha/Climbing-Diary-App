package com.main.climbingdiary.Ui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.main.climbingdiary.MainActivity;
import com.main.climbingdiary.R;
import com.main.climbingdiary.SettingsActivity;

public class NavDrawer implements NavigationView.OnNavigationItemSelectedListener {
    private final int layoutId = R.id.drawer_layout;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    public NavDrawer(AppCompatActivity _activity){
        drawer = _activity.findViewById(layoutId);
        toggle = new ActionBarDrawerToggle(
                _activity, drawer, (Toolbar) _activity.findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = _activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.navdrawer_item_settings:
                Intent intent = new Intent(MainActivity.getMainAppContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.getMainActivity().startActivity(intent);
                return true;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
