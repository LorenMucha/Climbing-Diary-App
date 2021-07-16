package com.main.climbingdiary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.RouteRepository;

import kotlin.jvm.JvmClassMappingKt;


@SuppressLint("ValidFragment")
public class EditProjektDialog extends DialogFragment {

    private final RouteRepository<Projekt> routeRepository;

    public EditProjektDialog(String title, int _id) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("id", _id);
        this.setArguments(args);
        this.routeRepository = new RouteRepository<>(JvmClassMappingKt.getKotlinClass(Projekt.class));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();

        // Fetch arguments from bundle and set title
        assert getArguments() != null;
        String title = getArguments().getString("title", "Bearbeiten");
        //get the route value which will be edit
        int route_id = getArguments().getInt("id", 0);
        Projekt editProjekt = routeRepository.getRoute(route_id);
        RouteDialogCreator creator = new RouteDialogCreator(view, _context, this);
        creator.setForeGroundSpan(title);
        creator.setUiElements(editProjekt);

        //save the route
        creator.getSaveRoute().setOnClickListener(v -> {
            Projekt projekt = creator.getProjekt(true);
            boolean taskState = routeRepository.deleteRoute(editProjekt);
            if (taskState) {
                routeRepository.insertRoute(projekt);
            }
            //close the dialog
            getDialog().cancel();
            FragmentPager.getInstance().refreshAllFragments();
        });

    }
}

