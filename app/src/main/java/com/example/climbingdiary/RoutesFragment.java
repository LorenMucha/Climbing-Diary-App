package com.example.climbingdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;

import com.example.climbingdiary.adapter.RoutesAdapter;
import com.example.climbingdiary.models.Route;

public class RoutesFragment extends Fragment {

    ArrayList<Route> routes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.routes_fragment, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvRoutes = (RecyclerView) view.findViewById(R.id.rvRoutes);

        // Initialize contacts
        try {
            routes = Route.getRouteList(getActivity().getBaseContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Create adapter passing in the sample user data
        RoutesAdapter adapter = new RoutesAdapter(routes);
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.setAdapter(adapter);
        // Set layout manager to position the items
        rvRoutes.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        return view;
    }
}
