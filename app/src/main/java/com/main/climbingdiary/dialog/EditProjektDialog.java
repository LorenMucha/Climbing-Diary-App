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
import com.main.climbingdiary.database.entities.ProjektRepository;


@SuppressLint("ValidFragment")
public class EditProjektDialog extends DialogFragment {

    public EditProjektDialog(String title, int _id) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("id", _id);
        this.setArguments(args);
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
        String title = getArguments().getString("title", "Bearbeiten");
        //get the route value which will be edit
        int route_id = getArguments().getInt("id", 0);
        Projekt editProjekt = ProjektRepository.getProjekt(route_id);
        RouteDialogCreator creator = new RouteDialogCreator(view, _context, this);
        creator.setForeGroundSpan(title);
        creator.setUiElements(editProjekt);

        //save the route
        creator.getSaveRoute().setOnClickListener(v -> {
            Projekt projekt = creator.getProjekt(true);
            boolean taskState = ProjektRepository.deleteProjekt(route_id);
            if (taskState) {
                ProjektRepository.insertProjekt(projekt);
            }
            //close the dialog
            getDialog().cancel();
            FragmentPager.refreshAllFragments();
        });

    }
}

