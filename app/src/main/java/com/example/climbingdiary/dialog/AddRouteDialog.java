package com.example.climbingdiary.dialog;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.climbingdiary.R;
import com.example.climbingdiary.models.Styles;


public class AddRouteDialog extends DialogFragment{

    public AddRouteDialog() {
    }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

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
        Button closeDialog = (Button) view.findViewById(R.id.input_route_close);
        Spinner spinner = (Spinner) view.findViewById(R.id.input_route_stil);
        EditText date = (EditText) view.findViewById(R.id.input_route_date);
        EditText name = (EditText) view.findViewById(R.id.input_route_name);
        // set Spinner for choosing the style
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, Styles.getStyle(true));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

        //set the date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                /*DatePickerDialog dialog = new DatePickerDialog(view.getContext(), v.getContext(), 2013, 2, 18);
                dialog.show();*/
            }
        });

        //close the dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

    }
}
