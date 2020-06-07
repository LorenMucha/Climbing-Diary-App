package com.main.climbingdiary.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.GradeConverter;
import com.main.climbingdiary.common.StringUtil;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.SetDate;
import com.main.climbingdiary.controller.TimeSlider;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.database.entities.SectorRepository;
import com.main.climbingdiary.models.Alerts;
import com.main.climbingdiary.models.Levels;
import com.main.climbingdiary.models.Rating;
import com.main.climbingdiary.models.Styles;

public class AddRouteDialog extends DialogFragment{

    public AddRouteDialog() {}
    public static AddRouteDialog newInstance(String title){
        AddRouteDialog add = new  AddRouteDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        add.setArguments(args);
        return add;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();

        EditRouteCreator creator = new EditRouteCreator(view,_context,this);
        creator.setUiElements();

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
