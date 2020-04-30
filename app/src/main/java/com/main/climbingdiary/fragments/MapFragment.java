package com.main.climbingdiary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.BuildConfig;
import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.MapController;

import org.osmdroid.config.Configuration;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapFragment extends Fragment implements RouteFragment{

    public static final String TITLE = "Map";
    private static MapFragment INSTACE;
    private MapController mapController;

    public static MapFragment getInstance(){
        if(INSTACE==null){
            INSTACE = new MapFragment();
        }
        return INSTACE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mapController = new MapController(view);
        mapController.setUpMap();
        return view;
    }

    @Override
    public void refreshData() {
        mapController.refreshMap();
    }
}