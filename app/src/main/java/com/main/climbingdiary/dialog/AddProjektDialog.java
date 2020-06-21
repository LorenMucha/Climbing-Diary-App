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
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.ProjektRepository;
import com.main.climbingdiary.common.AlertManager;

@SuppressLint("ValidFragment")
public class AddProjektDialog extends DialogFragment {

    public AddProjektDialog(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        this.setArguments(args);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();

        RouteDialogCreator creator = new RouteDialogCreator(view,_context,this);
        creator.setUiElements(true);

        // Fetch arguments from bundle and set title
        if (getArguments() == null) throw new AssertionError();
        String title = getArguments().getString("title", "Neues Projekt");

        creator.setForeGroundSpan(title);

        creator.getSaveRoute().setOnClickListener(v -> {
            Projekt newProjekt = creator.getProjekt(false);
            boolean taskState = ProjektRepository.insertProjekt(newProjekt);
            if (taskState) {
                FragmentPager.refreshAllFragments();
            } else {
                AlertManager.setErrorAlert(view.getContext());
            }
            //close the dialog
            getDialog().cancel();
        });
    }
}
