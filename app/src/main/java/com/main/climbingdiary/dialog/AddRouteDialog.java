package com.main.climbingdiary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.TimeSlider;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;

import kotlin.jvm.JvmClassMappingKt;

@SuppressLint("ValidFragment")
public class AddRouteDialog extends DialogFragment {

    private final RouteRepository<Route> routeRepository;

    public AddRouteDialog(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        this.setArguments(args);
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

        Log.d("State set:", AppPreferenceManager.INSTANCE.getSportType().typeToString());

        RouteDialogCreator creator = new RouteDialogCreator(view, _context, this);
        creator.setUiElements(false);

        // Fetch arguments from bundle and set title
        assert getArguments() != null;
        String title = getArguments().getString("title", "Neue Kletterroute");

        creator.setForeGroundSpan(title);

        //save the route
        creator.getSaveRoute().setOnClickListener(v -> {
            if (creator.checkDate()) {
                Route newRoute = creator.getRoute(false);
                boolean taskState = routeRepository.insertRoute(newRoute);
                if (taskState) {
                    FragmentPager.INSTANCE.refreshAllFragments();
                } else {
                    AlertManager.setErrorAlert(view.getContext());
                }
                TimeSlider.INSTANCE.setTimes();
                //close the dialog
                getDialog().cancel();
            }
        });
    }
}
