package com.main.climbingdiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.Ui.TimeSlider;
import com.main.climbingdiary.Ui.button.AddRoute;
import com.main.climbingdiary.Ui.button.ShowTimeSlider;
import com.main.climbingdiary.abstraction.RouteFragment;
import com.main.climbingdiary.adapter.ProjektAdapter;
import com.main.climbingdiary.models.Projekt;

import java.text.ParseException;
import java.util.ArrayList;

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

        // Initialize contacts
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
        rvProjekte.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        setHasOptionsMenu(true);
        return view;
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
}
