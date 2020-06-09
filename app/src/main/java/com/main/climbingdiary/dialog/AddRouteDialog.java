package com.main.climbingdiary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.TimeSlider;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.Alerts;

public class AddRouteDialog extends DialogFragment{

    private static AddRouteDialog INSTANZ;

    public AddRouteDialog() {}
    public static AddRouteDialog newInstance(String title){
        if(INSTANZ==null) {
            AddRouteDialog add = new AddRouteDialog();
            Bundle args = new Bundle();
            args.putString("title", title);
            add.setArguments(args);
            INSTANZ = add;
        }
        return INSTANZ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();

        RouteDialogCreator creator = new RouteDialogCreator(view,_context,this);
        creator.setUiElements(false);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Neue Kletterroute");

        creator.setForeGroundSpan(title);

        //save the route
        creator.getSaveRoute().setOnClickListener(v -> {
            Route newRoute = creator.getRoute(false);
            boolean taskState = RouteRepository.insertRoute(newRoute);
            if(taskState){
                FragmentPager.refreshAllFragments();
            }else{
                Alerts.setErrorAlert(view.getContext());
            }
            new TimeSlider();
            //close the dialog
            getDialog().cancel();
        });
    }
}
