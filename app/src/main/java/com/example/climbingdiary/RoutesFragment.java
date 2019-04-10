package com.example.climbingdiary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import com.example.climbingdiary.adapter.RoutesAdapter;
import com.example.climbingdiary.models.Route;

public class RoutesFragment extends Fragment {

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
            routes = Route.getRouteList(getActivity().getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Create adapter passing in the sample user data
        adapter = new RoutesAdapter(routes);
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.setAdapter(adapter);
        // Set layout manager to position the items
        rvRoutes.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return view;
    }

    public static RoutesAdapter getAdapter(){
        return adapter;
    }

    public static void refreshData(){
        ArrayList<Route> routes = new ArrayList<Route>();
        try {
            routes = Route.getRouteList(MainActivity.getAppContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapter = new RoutesAdapter(routes);
        rvRoutes.setAdapter(adapter);
        StatisticFragment.createBarChart();
    }

}
