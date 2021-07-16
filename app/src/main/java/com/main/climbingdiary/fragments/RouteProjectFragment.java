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
import com.main.climbingdiary.adapter.ProjektAdapter;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.AppBarMenu;
import com.main.climbingdiary.controller.FilterHeader;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.RouteSort;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.jvm.JvmClassMappingKt;
import lombok.Getter;
import lombok.Setter;

public class RouteProjectFragment extends Fragment implements RouteFragment {

    private static ProjektAdapter adapter;
    @Getter
    @Setter
    private static boolean filter_checked = false;
    @SuppressLint("StaticFieldLeak")
    private static RouteProjectFragment INSTANZ = null;
    private final RouteRepository<Projekt> routeRepository;
    public View view;
    private RecyclerView rvProjekte;
    @SuppressLint("StaticFieldLeak")
    private FilterHeader header;
    private ArrayList<Projekt> projekts;

    @SuppressLint("ValidFragment")
    private RouteProjectFragment() {
        this.routeRepository = new RouteRepository<>(JvmClassMappingKt.getKotlinClass(Projekt.class));
    }

    public static RouteProjectFragment getInstance() {
        if (INSTANZ == null) {
            INSTANZ = new RouteProjectFragment();
        }
        return INSTANZ;
    }

    public static synchronized ProjektAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.project_fragment, container, false);
        // Lookup the recyclerview in activity layout
        rvProjekte = view.findViewById(R.id.rvProjekte);
        AppPreferenceManager.removeAllFilterPrefs();
        // Initialize projects
        projekts = routeRepository.getRouteList();
        // Create adapter passing in the sample user data
        adapter = new ProjektAdapter(projekts);
        // Attach the adapter to the recyclerview to populate items
        rvProjekte.setAdapter(adapter);
        // Set layout manager to position the items
        rvProjekte.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext()));
        setHasOptionsMenu(true);
        header = new FilterHeader(this);
        return view;
    }

    @Override
    public void refreshData() {
        projekts.clear();
        projekts = routeRepository.getRouteList();
        adapter = new ProjektAdapter(projekts);
        rvProjekte.setAdapter(adapter);
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
            //Filter magic here ;-)
            header.show(item.getTitle().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
