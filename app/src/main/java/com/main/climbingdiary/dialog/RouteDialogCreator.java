package com.main.climbingdiary.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.GradeConverter;
import com.main.climbingdiary.common.RouteConverter;
import com.main.climbingdiary.common.preferences.AppPreferenceManager;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.controller.SetDate;
import com.main.climbingdiary.database.entities.AreaRepository;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.SectorRepository;
import com.main.climbingdiary.models.Alert;
import com.main.climbingdiary.models.Levels;
import com.main.climbingdiary.models.Rating;
import com.main.climbingdiary.models.SportType;
import com.main.climbingdiary.models.Styles;
import com.main.climbingdiary.models.Tabs;

import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lombok.Getter;

public class RouteDialogCreator {

    @Getter
    private final Spinner stil;
    @Getter
    private final Spinner level;
    @Getter
    private final Spinner rating;
    @Getter
    private final EditText date;
    @Getter
    private final EditText name;
    private final TextView nameHeader;
    @Getter
    private final AutoCompleteTextView area;
    @Getter
    private final AutoCompleteTextView sector;
    @Getter
    private final EditText comment;
    @Getter
    private final Context _context;
    private final View view;
    private final Switch gradeSwitcher;
    private final DialogFragment fragment;
    private final LinearLayout routeContent;
    private final LinearLayout gradeSwitcherLayout;
    @Getter
    Button closeDialog;
    @Getter
    Button saveRoute;

    public RouteDialogCreator(View _view, Context context, DialogFragment _fragment) {
        view = _view;
        closeDialog = view.findViewById(R.id.input_route_close);
        saveRoute = view.findViewById(R.id.input_route_save);
        stil = view.findViewById(R.id.input_route_stil);
        level = view.findViewById(R.id.input_route_level);
        rating = view.findViewById(R.id.input_route_rating);
        date = view.findViewById(R.id.input_route_date);
        name = view.findViewById(R.id.input_route_name);
        nameHeader = view.findViewById(R.id.input_route_name_header);
        area = view.findViewById(R.id.input_route_area);
        sector = view.findViewById(R.id.input_route_sektor);
        comment = view.findViewById(R.id.input_route_comment);
        gradeSwitcher = view.findViewById(R.id.grade_system_switcher);
        routeContent = view.findViewById(R.id.route_content);
        gradeSwitcherLayout = view.findViewById(R.id.grade_switcher_layout);
        fragment = _fragment;
        _context = context;
        setOnCloseButton();
    }

    @SuppressLint("SetTextI18n")
    public void setUiElements(Projekt projekt) {

        saveRoute.setText("Update");
        name.setText(projekt.getName());
        this.setRouteNameHeaderText();
        // set Spinner for choosing the level
        this.setLevelSpinner(projekt.getLevel(), Levels.INSTANCE.getLevelsFrench());
        this.setGradeSwitcher(true);

        //get the route List and set autocomplete
        this.setAreaSectorAutoComplete();
        area.setText(projekt.getArea());
        sector.setText(projekt.getSector());
        comment.setText(projekt.getComment());
        // set the Spinner
        this.setRatingSpinner(projekt.getRating() - 1);
        new SetDate(date, _context);
        routeContent.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void setUiElements(Route route) {

        saveRoute.setText("Update");
        name.setText(route.getName());
        this.setRouteNameHeaderText();
        // set Spinner for choosing the style
        this.setStilSpinner(route.getStyle().toUpperCase());
        // set Spinner for choosing the level
        this.setLevelSpinner(route.getLevel(), Levels.INSTANCE.getLevelsFrench());

        //get the route List and set autocomplete
        this.setAreaSectorAutoComplete();
        area.setText(route.getArea());
        sector.setText(route.getSector());
        comment.setText(route.getComment());
        // set the Spinner
        this.setRatingSpinner(route.getRating() - 1);
        new SetDate(date, _context);

        this.setGradeSwitcher(false);

        //set date listener
        try {
            new SetDate(date, _context);
            StringBuilder sBuilder = new StringBuilder();
            String[] dateSplit = (route.getDate()).split(Pattern.quote("."));
            sBuilder.append(dateSplit[2]).append("-").append(dateSplit[1]).append("-").append(dateSplit[0]);
            date.setText(sBuilder.toString());
        } catch (Exception e) {
            Log.e("Error", e.toString());
            date.setText(route.getDate());
        }
    }

    public void setUiElements(boolean projekt) {
        if (projekt) {
            routeContent.setVisibility(View.GONE);
        }
        this.setStilSpinner("RP");
        this.setLevelSpinner("8a", Levels.INSTANCE.getLevelsFrench());
        this.setAreaSectorAutoComplete();
        this.setRatingSpinner(1);
        this.setGradeSwitcher(true);
        this.setRouteNameHeaderText();
        new SetDate(date, _context);
    }

    public void setForeGroundSpan(String title) {
        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        fragment.getDialog().setTitle(ssBuilder);

        // Set title divider color
        int titleDividerId = fragment.getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = fragment.getDialog().findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(fragment.getResources().getColor(android.R.color.black));
    }

    private void setGradeSwitcher(boolean visibility) {
        int viewState = visibility && AppPreferenceManager.getSportType() == SportType.KLETTERN ? View.VISIBLE : View.GONE;

        gradeSwitcherLayout.setVisibility(viewState);
        gradeSwitcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                this.setLevelSpinner("8a", Levels.INSTANCE.getLevelsFrench());
            } else {
                this.setLevelSpinner("IX+/X-", Levels.INSTANCE.getLevelsUiaa());
            }
        });
    }

    private void setAreaSectorAutoComplete() {
        ArrayAdapter<String> areaArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, AreaRepository.INSTANCE.getAreaNameList());
        //will start working from first character
        area.setThreshold(1);
        area.setAdapter(areaArrayAdapter);
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayAdapter<String> sectorArrayAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, SectorRepository.INSTANCE.getSectorList(area.getText().toString().trim()));
                //will start working from first character
                sector.setThreshold(1);
                sector.setAdapter(sectorArrayAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setStilSpinner(String selection) {
        // set Spinner for choosing the style
        ArrayAdapter<String> stilArrayAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, Styles.INSTANCE.getStyle(true));
        stilArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        stil.setAdapter(stilArrayAdapter);

        try {
            stil.setSelection(stilArrayAdapter.getPosition(selection));
        } catch (Exception ex) {
            Log.e("Style was unset", ex.getLocalizedMessage());
        }
    }

    private void setLevelSpinner(String selection, String levels[]) {
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, levels);
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        level.setAdapter(levelArrayAdapter);
        level.setSelection(levelArrayAdapter.getPosition(selection));
    }

    private void setRatingSpinner(int selection) {
        // set Spinner for choosing the level
        ArrayAdapter<String> ratingArrayAdapter = new ArrayAdapter<String>(_context, android.R.layout.simple_spinner_item, Rating.INSTANCE.getRating());
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        rating.setAdapter(ratingArrayAdapter);
        rating.setSelection(selection);
    }

    private void setOnCloseButton() {
        //close the dialog
        getCloseDialog().setOnClickListener(v -> {
            if (AppPreferenceManager.getSelectedTabsTitle() == Tabs.PROJEKTE) {
                FragmentPager.getInstance().refreshSelectedFragment();
            }
            fragment.getDialog().cancel();
        });
    }

    private void setRouteNameHeaderText() {
        String header = AppPreferenceManager.getSportType().getRouteName() + "name";
        nameHeader.setText(header);
    }

    public Route getRoute(boolean id) {
        Route newRoute = new Route();
        if (id) {
            newRoute.setId(0);
        }
        String level = this.getLevel().getSelectedItem().toString();
        try {
            level = gradeSwitcher.isChecked() ? level : GradeConverter.convertUiaaToFrench(level);
        } catch (Exception ignored) {
        }
        newRoute.setName(this.getName().getText().toString());
        newRoute.setLevel(level);
        newRoute.setDate(this.getDate().getText().toString());
        newRoute.setArea(this.getArea().getText().toString());
        newRoute.setSector(this.getSector().getText().toString());
        newRoute.setComment(this.getComment().getText().toString());
        newRoute.setRating(this.getRating().getSelectedItemPosition() + 1);
        newRoute.setStyle(this.getStil().getSelectedItem().toString());
        return (Route) RouteConverter.cleanRoute(newRoute);
    }

    public Projekt getProjekt(boolean id) {
        Projekt projekt = new Projekt();
        if (id) {
            projekt.setId(0);
        }
        String level = this.getLevel().getSelectedItem().toString();
        try {
            level = gradeSwitcher.isChecked() ? level : GradeConverter.convertUiaaToFrench(level);
        } catch (Exception ignored) {
        }
        projekt.setName(this.getName().getText().toString());
        projekt.setLevel(level);
        projekt.setArea(this.getArea().getText().toString());
        projekt.setSector(this.getSector().getText().toString());
        projekt.setComment(this.getComment().getText().toString());
        projekt.setRating(this.getRating().getSelectedItemPosition() + 1);
        return (Projekt) RouteConverter.cleanRoute(projekt);
    }

    public boolean checkDate() {
        if (date.getText().toString().trim().length() == 0) {
            Alert alert = new Alert.Builder()
                    .title(String.format("Datum fehlt %s", "\ud83d\ude13"))
                    .dialogType(SweetAlertDialog.ERROR_TYPE)
                    .build();
            new AlertManager().setAlertWithoutContent(_context, alert);
            return false;
        } else {
            return true;
        }
    }
}
