package com.main.climbingdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.Ui.AppBarMenu;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.abstraction.RouteFragment;
import com.main.climbingdiary.adapter.ProjektAdapter;
import com.main.climbingdiary.models.data.Projekt;
import com.main.climbingdiary.models.RouteSort;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class RouteProjectFragment extends Fragment implements RouteFragment {

    private ArrayList<Projekt> projekts;
    private static ProjektAdapter adapter;
    private static RecyclerView rvProjekte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AddRoute.show();
        View view = inflater.inflate(R.layout.project_fragment, container, false);

        // Lookup the recyclerview in activity layout
        rvProjekte = (RecyclerView) view.findViewById(R.id.rvProjekte);

        // Initialize projects
        try {
            projekts = Projekt.getProjektList();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Create adapter passing in the sample user data
        adapter = new ProjektAdapter(projekts);
        // Attach the adapter to the recyclerview to populate items
        rvProjekte.setAdapter(adapter);
        // Set layout manager to position the items
        rvProjekte.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        setHasOptionsMenu(true);
        return view;
    }

    public static ProjektAdapter getAdapter(){
        return adapter;
    }

    public static void refreshData(){
        ArrayList<Projekt> projekts = new ArrayList<Projekt>();
        try {
            projekts = Projekt.getProjektList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new ProjektAdapter(projekts);
        rvProjekte.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.showItem();
        appmenu.hideItem("date");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.sort_level:
                RouteSort.setSort("level");
                RouteProjectFragment.refreshData();
                return true;
            case R.id.sort_area:
                RouteSort.setSort("area");
                RouteProjectFragment.refreshData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
