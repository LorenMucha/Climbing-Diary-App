package com.main.climbingdiary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.RouteConverter;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.Tabs;

import java.util.concurrent.atomic.AtomicBoolean;

import kotlin.jvm.JvmClassMappingKt;

/*
Fixme: switcher level noch anpassen
 */

@SuppressLint("ValidFragment")
public class EditRouteDialog extends DialogFragment {

    private Route route;
    private int route_id;
    private final String title;
    private final RouteRepository<Route> routeRepository;

    public EditRouteDialog(String _title, int _id){
            route_id = _id;
            title = _title;
            this.routeRepository = new RouteRepository<>(JvmClassMappingKt.getKotlinClass(Route.class));
    }

    public EditRouteDialog(String _title, Route _route) {
        route = _route;
        title = _title;
        this.routeRepository = new RouteRepository<>(JvmClassMappingKt.getKotlinClass(Route.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();
        AtomicBoolean taskState = new AtomicBoolean(false);
        Route editRoute;

        //get the route value which will be edit
        if(route != null){
            editRoute = route;
        }else{
            editRoute = routeRepository.getRoute(route_id);
        }

        RouteDialogCreator creator = new RouteDialogCreator(view,_context,this);
        creator.setForeGroundSpan(title);
        creator.setUiElements(editRoute);

        creator.getSaveRoute().setOnClickListener(v -> {
            //save ticked Project on if else save updated Route
            if(AppPreferenceManager.getSelectedTabsTitle() == Tabs.PROJEKTE){
                FragmentPager.getInstance().setPosition(1);
                //needs to be converted to a project
                routeRepository.deleteRoute(RouteConverter.routeToProjekt(editRoute));
                taskState.set(routeRepository.insertRoute(creator.getRoute(true)));
            }else{
                Route updateRoute = creator.getRoute(false);
                updateRoute.setId(editRoute.getId());
                taskState.set(routeRepository.updateRoute(updateRoute));
            }
            //close the dialog
            getDialog().cancel();
            if (taskState.get()) {
                FragmentPager.getInstance().refreshAllFragments();
            } else {
                AlertManager.setErrorAlert(view.getContext());
            }
        });
    }
}
