package com.main.climbingdiary;

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

import com.main.climbingdiary.Ui.menu.AppBarMenu;
import com.main.climbingdiary.Ui.header.FilterHeader;
import com.main.climbingdiary.adapter.RoutesAdapter;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.RouteSort;

import java.util.ArrayList;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

public class RouteDoneFragment extends Fragment implements RouteFragment {

    private ArrayList<Route> routes;
    private static RoutesAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerView rvRoutes;
    @SuppressLint("StaticFieldLeak")
    private static FilterHeader filterHeader;
    @Getter
    @Setter
    private static boolean filter_checked = false;
    @SuppressLint("StaticFieldLeak")
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.routes_fragment, container, false);

        // Lookup the recyclerview in activity layout
        rvRoutes = (RecyclerView) view.findViewById(R.id.rvRoutes);

        // Initialize routes
        routes = RouteRepository.getRouteList();
        // Create adapter passing in the sample user data
        adapter = new RoutesAdapter(routes);
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.setAdapter(adapter);
        // Set layout manager to position the items
        rvRoutes.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        setHasOptionsMenu(true);
        setFilterMenu();
        return view;
    }

    public static RoutesAdapter getAdapter(){
        return adapter;
    }

    public static void refreshData(){
        ArrayList<Route> routes;
        try {
            routes = RouteRepository.getRouteList();
            adapter = new RoutesAdapter(routes);
            rvRoutes.setAdapter(adapter);
        }catch(Exception ex){}
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.setItemVisebility(true);
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
