package com.main.climbingdiary;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.Ui.NavDrawer;
import com.main.climbingdiary.Ui.TimeSlider;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.Ui.button.ShowTimeSlider;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int layoutId = R.layout.activity_main;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static ComponentName componentName;
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity mainActivity;

    public static Context getMainAppContext() {
        return MainActivity.context;
    }
    public static ComponentName getMainComponentName(){ return MainActivity.componentName;}
    public static AppCompatActivity getMainActivity(){return MainActivity.mainActivity;};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivity.context = getApplicationContext();
        MainActivity.componentName = getComponentName();
        MainActivity.mainActivity = this;

        //manager for the tabs
        FragmentPager fragmentPager = new FragmentPager(this);
        fragmentPager.setFragmente();

        //the add buttons
        new AddRoute(this);
        new ShowTimeSlider(this);

        //the slider
        new TimeSlider(this);

        //navigation View
        new NavDrawer(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
