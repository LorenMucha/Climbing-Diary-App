package com.main.climbingdiary.fragments;

import android.annotation.SuppressLint;
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

import com.main.climbingdiary.R;
import com.main.climbingdiary.view.button.AddRoute;
import com.main.climbingdiary.view.header.FilterHeader;
import com.main.climbingdiary.view.menu.AppBarMenu;
import com.main.climbingdiary.view.menu.MenuValues;
import com.main.climbingdiary.adapter.ProjektAdapter;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.ProjektRepository;
import com.main.climbingdiary.models.RouteSort;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class RouteProjectFragment extends Fragment implements RouteFragment {

    private ArrayList<Projekt> projekts;
    private static ProjektAdapter adapter;
    private static RecyclerView rvProjekte;
    @SuppressLint("StaticFieldLeak")
    private static FilterHeader filterHeader;
    @Getter
    @Setter
    private static boolean filter_checked = false;
    @SuppressLint("StaticFieldLeak")
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AddRoute.show();
        view = inflater.inflate(R.layout.project_fragment, container, false);

        // Lookup the recyclerview in activity layout
        rvProjekte = (RecyclerView) view.findViewById(R.id.rvProjekte);

        // Initialize projects
        try {
            projekts = ProjektRepository.getProjektList();
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
        setFilterMenu();
        return view;
    }

    public static ProjektAdapter getAdapter(){
        return adapter;
    }

    public static void refreshData(){
        ArrayList<Projekt> projekts;
        try {
            projekts = ProjektRepository.getProjektList();
            adapter = new ProjektAdapter(projekts);
            rvProjekte.setAdapter(adapter);
        } catch (Exception e) {}

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.setItemVisebility(MenuValues.SORT_DATE,false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        if(id==R.id.sort_level){
            RouteSort.setSort("level");
            refreshData();
            return true;
        }
        else if(id==R.id.sort_area){
            RouteSort.setSort("area");
            refreshData();
            return true;
        }
        else if(id==R.id.sort_date) {
            RouteSort.setSort("date");
            refreshData();
            return true;
        }
        else if(item.getGroupId()==R.id.filter_area+1){
            //Filter magic here ;-)
            filterHeader.show(item.getTitle().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void setFilterMenu(){
        filterHeader = new FilterHeader(view);
    }
}