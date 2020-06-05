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

        // Fetch arguments from bundle and set title

        String title = getArguments().getString("title", "Neue Kletterroute");

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan,0,title.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        getDialog().setTitle(ssBuilder);

        // Set title divider color
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(android.R.color.black));

        //input elements
        Button closeDialog = view.findViewById(R.id.input_route_close);
        Button saveRoute = view.findViewById(R.id.input_route_save);
        final Spinner stil = view.findViewById(R.id.input_route_stil);
        Spinner level = view.findViewById(R.id.input_route_level);
        final Switch gradeSwitcher = view.findViewById(R.id.grade_system_switcher);
        final Spinner rating = (Spinner) view.findViewById(R.id.input_route_rating);
        final EditText date = (EditText) view.findViewById(R.id.input_route_date);
        final EditText name = (EditText) view.findViewById(R.id.input_route_name);
        final AutoCompleteTextView area = (AutoCompleteTextView) view.findViewById(R.id.input_route_area);
        final AutoCompleteTextView sector = (AutoCompleteTextView) view.findViewById(R.id.input_route_sektor);
        final EditText comment = (EditText) view.findViewById(R.id.input_route_comment);

        // set Spinner for choosing the style
        ArrayAdapter<String> stilArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Styles.getStyle(true));
        stilArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        stil.setAdapter(stilArrayAdapter);

        // set Spinner for choosing the level
        ArrayAdapter<String> levelArrayAdapterFrench = new ArrayAdapter<>(_context,android.R.layout.simple_spinner_item, Levels.getLevelsFrench());
        ArrayAdapter<String> levelArrayAdapterUiaa = new ArrayAdapter<>(_context,android.R.layout.simple_spinner_item, Levels.getLevelsUiaa());
        levelArrayAdapterFrench.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        level.setAdapter(levelArrayAdapterFrench);
        //pre select 8a
        level.setSelection(12);

        //get the route List and set autocomplete
        ArrayAdapter<String> areaArrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, AreaRepository.getAreaNameList());
        //will start working from first character
        area.setThreshold(1);
        area.setAdapter(areaArrayAdapter);

        //get the sector list and set the autocomplete if area is set
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("sector list",TextUtils.join(",", SectorRepository.getSectorList(_context,area.getText().toString().trim())));
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

        //set date listener
        new SetDate(date, _context);

        //set switch callback
        gradeSwitcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                level.setAdapter(levelArrayAdapterFrench);
            }else{
                level.setAdapter(levelArrayAdapterUiaa);
            }
        });

        //save the route
        saveRoute.setOnClickListener(v -> {
            Route newRoute = new Route();
            String level_text = level.getSelectedItem().toString();
            level_text = gradeSwitcher.isChecked() ?  level_text : GradeConverter.convertUiaaToFrech(level_text);
            newRoute.setName(StringUtil.cleanDbString(name.getText().toString()));
            newRoute.setLevel(level_text);
            newRoute.setDate(date.getText().toString());
            newRoute.setArea(area.getText().toString());
            newRoute.setSector(sector.getText().toString());
            newRoute.setComment(comment.getText().toString());
            newRoute.setRating(rating.getSelectedItemPosition()+1);
            newRoute.setStyle(stil.getSelectedItem().toString());
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

        //close the dialog
        closeDialog.setOnClickListener(v -> getDialog().cancel());

    }
}
