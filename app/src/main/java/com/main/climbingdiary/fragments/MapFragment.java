package com.main.climbingdiary.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.BuildConfig;
import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.models.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapFragment extends Fragment {
    private View view;
    private static GeoPoint startPoint = new GeoPoint(49.58,11.01);
    private static MapView map;
    private static IMapController mapController;

    public static final String TITLE = "Map";
    private static MapFragment INSTACE;

    public static MapFragment getInstance(){
        if(INSTACE==null){
            INSTACE = new MapFragment();
        }
        return INSTACE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setUpMap();
        return view;
    }

    private void setUpMap(){
        map = view.findViewById(R.id.map);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);
    }

    public static void setUserPosition(){
        GpsMyLocationProvider prov = new GpsMyLocationProvider(MainActivity.getMainAppContext());
        prov.addLocationSource(LocationManager.GPS_PROVIDER);
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(prov, map);
        mapController.setZoom(14);
        locationOverlay.runOnFirstFix(() -> {
            final GeoPoint myLocation = locationOverlay.getMyLocation();
            if (myLocation != null) {
                MainActivity.getMainActivity().runOnUiThread(() -> mapController.animateTo(myLocation));
            };
        });
        locationOverlay.setOptionsMenuEnabled(true);
        map.getOverlays().add(locationOverlay);
    }
}