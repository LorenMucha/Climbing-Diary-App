package com.main.climbingdiary.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.adapter.RoutesAdapter;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.AppBarMenu;
import com.main.climbingdiary.controller.FilterHeader;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.RouteSort;

import java.util.ArrayList;
import java.util.Objects;

public class RouteDoneFragment extends Fragment implements RouteFragment {

    private static RoutesAdapter adapter;
    private RecyclerView rvRoutes;
    private View view;
    private FilterHeader header;
    private final RouteRepository<Route> routeRepository;

    @SuppressLint("StaticFieldLeak")
    private static RouteDoneFragment INSTANZ = null;
    private ArrayList<Route> routes;

    @SuppressLint("ValidFragment")
    private RouteDoneFragment() {
        this.routeRepository = new RouteRepository<>(Route.class);
    }

    public static RouteDoneFragment getInstance() {
        if (INSTANZ == null) {
            INSTANZ = new RouteDoneFragment();
        }
        return INSTANZ;
    }


    @Override
    public View getView() {
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.routes_fragment, container, false);
        //new FilterHeader(view);
        setHasOptionsMenu(true);
        header = new FilterHeader(this);
        // Lookup the recyclerview in activity layout
        rvRoutes = view.findViewById(R.id.rvRoutes);
        // Initialize routes
        routes = routeRepository.getRouteList();
        // Create adapter passing in the sample user data
        adapter = new RoutesAdapter(routes);
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.setAdapter(adapter);
        // Set layout manager to position the items
        rvRoutes.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        return view;
    }

    public static synchronized RoutesAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void refreshData() {
        routes.clear();
        routes = routeRepository.getRouteList();
        adapter = new RoutesAdapter(routes);
        rvRoutes.setAdapter(adapter);
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
        if (id == R.id.sort_level) {
            AppPreferenceManager.setSort(RouteSort.LEVEL);
            refreshData();
            return true;
        } else if (id == R.id.sort_area) {
            AppPreferenceManager.setSort(RouteSort.AREA);
            refreshData();
            return true;
        } else if (id == R.id.sort_date) {
            AppPreferenceManager.setSort(RouteSort.DATE);
            refreshData();
            return true;
        } else if (item.getGroupId() == R.id.filter_area + 1) {
            header.show(item.getTitle().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
