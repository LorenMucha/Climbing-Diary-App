package com.main.climbingdiary.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;

import com.main.climbingdiary.R;
import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.controller.header.FilterHeader;
import com.main.climbingdiary.controller.menu.MenuValues;
import com.main.climbingdiary.database.entities.Area;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.models.Filter;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

import lombok.RequiredArgsConstructor;

import static android.content.Context.LOCATION_SERVICE;

@RequiredArgsConstructor
public class MapController {

    private final View view;
    private static MapView map;
    private static IMapController mapController;
    private static GeoPoint startPoint = new GeoPoint(49.58, 11.01);
    private static LocationManager locationManager = (LocationManager) MainActivity.getMainActivity().getSystemService(LOCATION_SERVICE);


    public void setUpMap() {
        map = view.findViewById(R.id.map);
        mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);
        setMarker();

    }

    public static void setUserPosition() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            GpsMyLocationProvider prov = new GpsMyLocationProvider(map.getContext());
            prov.addLocationSource(LocationManager.GPS_PROVIDER);
            MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(prov, map);
            locationOverlay.enableFollowLocation();
            locationOverlay.enableMyLocation();
            mapController.setZoom(14);
            locationOverlay.runOnFirstFix(() -> {
                final GeoPoint myLocation = locationOverlay.getMyLocation();
                if (myLocation != null) {
                    MainActivity.getMainActivity().runOnUiThread(() -> mapController.animateTo(myLocation));
                }
                ;
            });
            locationOverlay.setOptionsMenuEnabled(true);
            map.getOverlays().add(locationOverlay);
        } else {
            showGPSDisabledAlertToUser();
        }
    }

    private static void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(map.getContext());
        alertDialogBuilder.setMessage("GPS ist auf deinem Gerät nicht aktiviert. Möchtest du es aktivieren ?")
                .setCancelable(false)
                .setPositiveButton("Gehe zu den Einstellungen, um das GPS zu aktivieren",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            MainActivity.getMainActivity().startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void setMarker() {
        List<Area> areaList = AreaRepository.getAreaList();
        FolderOverlay poiMarkers = new FolderOverlay(map.getContext());
        map.getOverlays().add(poiMarkers);
        Drawable poiIcon = MainActivity.getMainActivity().getResources().getDrawable(R.drawable.ic_berg);
        for (Area area : areaList) {
            if (area.getLat() != 0.0 && area.getLng() != 0.0) {
                int sumAscents = RouteRepository.getRouteListByArea(area.getId()).size();
                Marker poiMarker = new Marker(map);
                poiMarker.setPosition(new GeoPoint(area.getLat(), area.getLng()));
                poiMarker.setIcon(poiIcon);
                poiMarker.setTitle(area.getName());
                poiMarker.setSnippet(String.format("Hier hast du %s Begehungen gemacht",sumAscents));
                poiMarkers.add(poiMarker);
                poiMarker.setInfoWindow(new CustomInfoWindow(map, area));
            }
        }
    }

    static class CustomInfoWindow extends MarkerInfoWindow {
        private Area area;
        Button btn = (mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo));
        CustomInfoWindow(MapView mapView, Area _area) {
            super(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, mapView);
            this.area = _area;
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    FragmentPager.setPosition(1);
                    Filter.setFilter(String.format("g.id = %s",area.getId()), MenuValues.FILTER);
                    RouteDoneFragment.refreshData();
                    new FilterHeader(RouteDoneFragment.view).show(area.getName());
                }
            });

        }
        @Override public void onOpen(Object item){
            super.onOpen(item);
            mView.findViewById(org.osmdroid.bonuspack.R.id.bubble_moreinfo).setVisibility(View.VISIBLE);

        }
    }
}
