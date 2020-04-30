package com.main.climbingdiary.controller.dialog;

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

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.SetDate;
import com.main.climbingdiary.controller.Tabs;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.ProjektRepository;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.database.entities.SectorRepository;
import com.main.climbingdiary.fragments.RouteDoneFragment;
import com.main.climbingdiary.fragments.RouteProjectFragment;
import com.main.climbingdiary.fragments.StatisticFragment;
import com.main.climbingdiary.models.Levels;
import com.main.climbingdiary.models.Rating;
import com.main.climbingdiary.models.Styles;

import java.util.regex.Pattern;

public class EditRouteDialog extends DialogFragment {
    private static Route route = null;
    private static int route_id;
    private static String title = null;
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

        Log.d("edit tick route",editRoute.toString());

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan,0,title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        getDialog().setTitle(ssBuilder);

        // Set title divider color
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(android.R.color.black));

        //input elements
        Button closeDialog = (Button) view.findViewById(R.id.input_route_close);
        Button saveRoute = (Button) view.findViewById(R.id.input_route_save);
        saveRoute.setText("Update");
        final Spinner stil = (Spinner) view.findViewById(R.id.input_route_stil);
        final Spinner level = (Spinner) view.findViewById(R.id.input_route_level);
        final Spinner rating = (Spinner) view.findViewById(R.id.input_route_rating);
        final EditText date = (EditText) view.findViewById(R.id.input_route_date);
        final EditText name = (EditText) view.findViewById(R.id.input_route_name);
        final AutoCompleteTextView area = (AutoCompleteTextView) view.findViewById(R.id.input_route_area);
        final AutoCompleteTextView sector = (AutoCompleteTextView) view.findViewById(R.id.input_route_sektor);
        final EditText comment = (EditText) view.findViewById(R.id.input_route_comment);

        name.setText(editRoute.getName());

        // set Spinner for choosing the style
        ArrayAdapter<String> stilArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Styles.getStyle(true));
        stilArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        stil.setAdapter(stilArrayAdapter);
        try {
            stil.setSelection(stilArrayAdapter.getPosition(editRoute.getStyle().toUpperCase()));
        }catch(Exception ex){
            Log.e("Style was unset",ex.getLocalizedMessage());
        }

        // set Spinner for choosing the level
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Levels.getLevels());
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        level.setAdapter(levelArrayAdapter);
        level.setSelection(levelArrayAdapter.getPosition(editRoute.getLevel()));

        //get the route List and set autocomplete
        ArrayAdapter<String> areaArrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, AreaRepository.getAreaNameList());
        //will start working from first character
        area.setThreshold(1);
        area.setAdapter(areaArrayAdapter);
        area.setText(editRoute.getArea());
        sector.setText(editRoute.getSector());
        comment.setText(editRoute.getComment());

        //get the sector list and set the autocomplete if area is set
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("sector list", TextUtils.join(",", SectorRepository.getSectorList(_context,area.getText().toString().trim())));
                ArrayAdapter<String> sectorArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, SectorRepository.getSectorList(_context,area.getText().toString().trim()));
                //will start working from first character
                sector.setThreshold(1);
                sector.setAdapter(sectorArrayAdapter);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set Spinner for choosing the level
        ArrayAdapter<String> ratingArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Rating.getRating());
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        rating.setAdapter(ratingArrayAdapter);
        rating.setSelection(editRoute.getRating()-1);

        //set date listener
        try {
            SetDate setDate = new SetDate(date, _context);
            StringBuilder sBuilder = new StringBuilder();
            String[] dateSplit = (editRoute.getDate()).split(Pattern.quote("."));
            sBuilder.append(dateSplit[2]).append("-").append(dateSplit[1]).append("-").append(dateSplit[0]);
            date.setText(sBuilder.toString());
        }catch(Exception e){
            Log.e("Error",e.toString());
            date.setText(route.getDate());
        }

        //save the route
        saveRoute.setOnClickListener(v -> {
            if(FragmentPager.getTabTitle().equals(Tabs.PROJEKTE.getTitle())){
                FragmentPager.setPosition(1);
                Projekt projekt = new Projekt();
                ProjektRepository.deleteProjekt(editRoute.getId());
                Log.d("delete Projekt with ID:",Integer.toString(editRoute.getId()));
                FragmentPager.refreshActualFragment();
            }
            Route newRoute = new Route();
            newRoute.setId(0);
            newRoute.setName(name.getText().toString());
            newRoute.setLevel(level.getSelectedItem().toString());
            newRoute.setDate(date.getText().toString());
            newRoute.setArea(area.getText().toString());
            newRoute.setSector(sector.getText().toString());
            newRoute.setComment(comment.getText().toString());
            newRoute.setRating(rating.getSelectedItemPosition()+1);
            newRoute.setStyle(stil.getSelectedItem().toString());
            Log.d("delete Route with ID", Integer.toString(editRoute.getId()));
            boolean taskState = RouteRepository.deleteRoute(editRoute.getId());
            if(taskState) {
                RouteRepository.insertRoute(newRoute);
            }
            //close the dialog
            getDialog().cancel();
            FragmentPager.refreshAllFragments();
        });

        //close the dialog
        closeDialog.setOnClickListener(v ->{
            if(FragmentPager.getTabTitle().equals(Tabs.PROJEKTE.getTitle())){
                FragmentPager.refreshActualFragment();
            }
                getDialog().cancel();
        });
    }
}
