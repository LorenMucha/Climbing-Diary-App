package com.main.climbingdiary;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.main.climbingdiary.Ui.FragmentPager;
import com.main.climbingdiary.Ui.NavDrawer;
import com.main.climbingdiary.dialog.DialogManager;

public class MainActivity extends AppCompatActivity {

    private static final int layoutId = R.layout.activity_main;
    private static Context context;
    private static ComponentName componentName;
    private FloatingActionButton addRoute;
    //manager for the tabs
    private FragmentPager fragmentPager;
    //navigation View
    private NavDrawer navDrawer;

    public static Context getMainAppContext() {
        return MainActivity.context;
    }
    public static ComponentName getMainComponentName(){ return MainActivity.componentName;}
    public static int getLayoutId(){return layoutId;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainActivity.context = getApplicationContext();
        MainActivity.componentName = getComponentName();

        fragmentPager = new FragmentPager(this);
        fragmentPager.setFragmente();

        //the add button
        addRoute = (FloatingActionButton) findViewById(R.id.addRoute);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.openAddRouteDialog(view.getContext());
            }
        });

        navDrawer = new NavDrawer(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
}
