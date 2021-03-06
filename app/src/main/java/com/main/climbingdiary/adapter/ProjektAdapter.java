package com.main.climbingdiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.models.Tabs;
import com.main.climbingdiary.controller.button.AppFloatingActionButton;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.dialog.DialogFactory;
import com.main.climbingdiary.database.entities.Projekt;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.models.Colors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProjektAdapter extends
        RecyclerView.Adapter<ProjektAdapter.ViewHolder> implements Filterable {

    private List<Projekt> mProjekts;
    private final List<Projekt> mProjekts_filtered;
    private final RouteRepository<Projekt> routeRepository;

    // Pass in the contact array into the constructor
    public ProjektAdapter(List<Projekt> projekts) {
        this.mProjekts = projekts;
        this.mProjekts_filtered = projekts;
        this.routeRepository = new RouteRepository<>(Projekt.class);
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ProjektAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View projectView = inflater.inflate(R.layout.item_projekt, parent, false);

        // Return a new holder instance
        return new ViewHolder(projectView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ProjektAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Projekt projekt = mProjekts.get(position);

        String gradeText = projekt.getLevel();
        String areaText = projekt.getArea();
        String sectorText = projekt.getSector();
        String commentString = projekt.getComment();

        //create the html string for the route and sector
        final String routeHtml = areaText + " &#9679; " + sectorText;
        final String commentHtml = "<b>Kommentar</b><br/>" + commentString;

        // Set item views
        TextView routeName = viewHolder.nameTextView;
        TextView level = viewHolder.levelTextView;
        TextView area = viewHolder.areaTextView;
        final TextView comment = viewHolder.commentTextView;
        final TableRow hidden_layout = viewHolder.hiddenView;

        //route manipulate buttons
        ImageButton edit = viewHolder.editButton;
        ImageButton delete = viewHolder.removeButton;
        CheckBox routeTick = viewHolder.checkProject;

        RatingBar rating = viewHolder.ratingView;
        routeName.setText(projekt.getName());
        level.setText(gradeText);
        level.setTextColor(Colors.getGradeColor(gradeText));

        area.setText(Html.fromHtml(routeHtml));
        comment.setText(Html.fromHtml(commentHtml));
        rating.setRating(projekt.getRating());

        //show comment on holder click
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            int click = 0;

            @Override
            public void onClick(View v) {
                if (click == 0) {
                    //if last element hide add Button
                    if (mProjekts.indexOf(projekt) == (mProjekts.size() - 1)) {
                        AppFloatingActionButton.hide();
                    }
                    hidden_layout.setVisibility(View.VISIBLE);
                    click++;
                } else {
                    //if last element show add Button
                    if (mProjekts.indexOf(projekt) == (mProjekts.size() - 1)) {
                        AppFloatingActionButton.show();
                    }
                    hidden_layout.setVisibility(View.GONE);
                    click = 0;
                }
            }
        });

        //delete a route
        delete.setOnClickListener(v -> {
            final View _v = v;
            new SweetAlertDialog(_v.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Bist du sicher ?")
                    .setConfirmText("OK")
                    .setCancelText("Abbrechen")
                    .setConfirmClickListener(sDialog -> {
                        //delete the route by id
                        boolean taskState = routeRepository.deleteRoute(projekt);
                        if (taskState) {
                            FragmentPager.getInstance().refreshSelectedFragment();
                            sDialog.hide();
                            new SweetAlertDialog(_v.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Gelöscht")
                                    .show();
                        } else {
                            AlertManager.setErrorAlert(v.getContext());
                        }

                    })
                    .setCancelButton("Cancel", sDialog -> sDialog.cancel())
                    .show();
        });
        //edit a route
        edit.setOnClickListener(view -> DialogFactory.openEditRouteDialog(Tabs.PROJEKTE, projekt.getId()));

        //tick projekt
        routeTick.setOnClickListener(view -> {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd", Locale.GERMAN);
            Route route = new Route();
            route.setId(projekt.getId());
            route.setName(projekt.getName());
            route.setArea(projekt.getArea());
            route.setLevel(projekt.getLevel());
            route.setSector(projekt.getSector());
            route.setDate(sdf.format(new Date()));
            route.setRating(projekt.getRating());
            route.setComment(projekt.getComment());
            route.setStyle("rp");
            Log.d("Tick projekt: ",projekt.toString());
            DialogFactory.openEditRouteDialog(route);
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mProjekts.size();
    }

    //filter for search view
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mProjekts = mProjekts_filtered;
                } else {
                    List<Projekt> filteredList = new ArrayList<>();
                    for (Projekt row : mProjekts) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLevel().contains(charString) ||
                                row.getArea().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSector().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    mProjekts = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mProjekts;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mProjekts = (ArrayList<Projekt>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        TextView levelTextView;
        TextView areaTextView;
        RatingBar ratingView;
        TextView commentTextView;
        TableRow hiddenView;
        ImageButton editButton;
        ImageButton removeButton;
        CheckBox checkProject;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        private ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.route_name);
            levelTextView = itemView.findViewById(R.id.route_level);
            areaTextView = itemView.findViewById(R.id.route_area);
            //styleTextView=(TextView) itemView.findViewById(R.id.route_style);
            ratingView = itemView.findViewById(R.id.route_rating);
            commentTextView = itemView.findViewById(R.id.route_comment);
            hiddenView = itemView.findViewById(R.id.route_hidden);
            editButton = itemView.findViewById(R.id.route_edit);
            removeButton = itemView.findViewById(R.id.route_delete);
            checkProject = itemView.findViewById(R.id.tick_project);
        }
    }
}