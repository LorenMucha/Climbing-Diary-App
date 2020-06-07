package com.main.climbingdiary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.Tabs;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.ProjektRepository;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;

/*
Fixme: switcher level noch anpassen
 */

public class EditRouteDialog extends DialogFragment {

    private static Route route;
    private static int route_id;
    private static String title;
    public EditRouteDialog() {}

    public static EditRouteDialog newInstance(String _title, int _id){
        Log.d("instanziate for id",Integer.toString(_id));
        EditRouteDialog edit = new  EditRouteDialog();
        route_id = _id;
        title = _title;
        return edit;
    }

    public static EditRouteDialog newInstance(String _title, Route _route){
        EditRouteDialog edit = new  EditRouteDialog();
        route = _route;
        title = _title;
        return edit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();
        Route editRoute;

        //get the route value which will be edit
        if(route != null){
            editRoute = route;
        }else{
            editRoute = RouteRepository.getRoute(route_id);
        }

        EditRouteCreator creator = new EditRouteCreator(view,_context,this);
        creator.setForeGroundSpan(title);
        creator.setUiElements(editRoute);

        //save the route
        creator.getSaveRoute().setOnClickListener(v -> {
            if(FragmentPager.getTabTitle().equals(Tabs.PROJEKTE.getTitle())){
                FragmentPager.setPosition(1);
                Projekt projekt = new Projekt();
                ProjektRepository.deleteProjekt(editRoute.getId());
                Log.d("delete Projekt with ID:",Integer.toString(editRoute.getId()));
                FragmentPager.refreshActualFragment();
            }
            Route newRoute = creator.getRoute(true);
            boolean taskState = RouteRepository.deleteRoute(editRoute.getId());
            if(taskState) {
                RouteRepository.insertRoute(newRoute);
            }
            //close the dialog
            getDialog().cancel();
            FragmentPager.refreshAllFragments();
        });
    }
}
