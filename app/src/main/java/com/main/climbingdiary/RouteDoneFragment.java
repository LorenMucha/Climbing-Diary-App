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
import com.main.climbingdiary.abstraction.RouteFragment;
import com.main.climbingdiary.adapter.RoutesAdapter;
import com.main.climbingdiary.models.Route;
import com.main.climbingdiary.models.RouteSort;

import java.text.ParseException;
import java.util.ArrayList;

public class RouteDoneFragment extends Fragment implements RouteFragment {

    private ArrayList<Route> routes;
    private static RoutesAdapter adapter;
    private static RecyclerView rvRoutes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.routes_fragment, container, false);

        // Lookup the recyclerview in activity layout
        rvRoutes = (RecyclerView) view.findViewById(R.id.rvRoutes);

        // Initialize contacts
        try {
            routes = Route.getRouteList();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Create adapter passing in the sample user data
        adapter = new RoutesAdapter(routes);
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.setAdapter(adapter);
        // Set layout manager to position the items
        rvRoutes.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        setHasOptionsMenu(true);

        return view;
    }

    public static RoutesAdapter getAdapter(){
        return adapter;
    }

    public static void refreshData(){
        ArrayList<Route> routes = new ArrayList<Route>();
        try {
            routes = Route.getRouteList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new RoutesAdapter(routes);
        rvRoutes.setAdapter(adapter);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        AppBarMenu appmenu = new AppBarMenu(menu);
        appmenu.showItem();
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
                RouteDoneFragment.refreshData();
                return true;
            case R.id.sort_area:
                RouteSort.setSort("area");
                RouteDoneFragment.refreshData();
                return true;
            case R.id.sort_date:
                RouteSort.setSort("date");
                RouteDoneFragment.refreshData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
