package com.main.climbingdiary.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.main.climbingdiary.R;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.controller.FragmentPager;
import com.main.climbingdiary.database.entities.Route;
import com.main.climbingdiary.database.entities.RouteRepository;
import com.main.climbingdiary.models.Tabs;
import com.main.climbingdiary.controller.button.AppFloatingActionButton;
import com.main.climbingdiary.dialog.DialogFactory;
import com.main.climbingdiary.models.Colors;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.jvm.JvmClassMappingKt;

public class RoutesAdapter extends
        RecyclerView.Adapter<RoutesAdapter.ViewHolder> implements Filterable {

    private List<Route> mRoutes;
    private final List<Route> mRoutes_filtered;
    private final RouteRepository<Route> routeRepository;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        TextView dateTextView;
        TextView levelTextView;
        TextView areaTextView;
        ImageView styleTextView;
        RatingBar ratingView;
        TextView commentTextView;
        TableRow hiddenView;
        ImageButton editButton;
        ImageButton removeButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        private ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.route_name);
            dateTextView = itemView.findViewById(R.id.route_date);
            levelTextView = itemView.findViewById(R.id.route_level);
            areaTextView = itemView.findViewById(R.id.route_area);
            //styleTextView=(TextView) itemView.findViewById(R.id.route_style);
            styleTextView = itemView.findViewById(R.id.route_style);
            ratingView = itemView.findViewById(R.id.route_rating);
            commentTextView = itemView.findViewById(R.id.route_comment);
            hiddenView = itemView.findViewById(R.id.route_hidden);
            editButton = itemView.findViewById(R.id.route_edit);
            removeButton = itemView.findViewById(R.id.route_delete);
        }
    }

    // Pass in the contact array into the constructor
    public RoutesAdapter(List<Route> routes) {
        this.mRoutes = routes;
        this.mRoutes_filtered = routes;
        this.routeRepository = new RouteRepository<>(JvmClassMappingKt.getKotlinClass(Route.class));
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_route, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RoutesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Route route = mRoutes.get(position);

        String gradeText = route.getLevel();
        String styleText = route.getStyle();
        String areaText = route.getArea();
        String sectorText = route.getSector();
        String commentString = route.getComment();

        //create the html string for the route and sector
        final String routeHtml = areaText + " &#9679; " + sectorText;
        final String commentHtml = "<b>Kommentar</b><br/>" + commentString;

        // Set item views
        TextView routeName = viewHolder.nameTextView;
        TextView date = viewHolder.dateTextView;
        TextView level = viewHolder.levelTextView;
        ImageView style = viewHolder.styleTextView;
        TextView area = viewHolder.areaTextView;
        final TextView comment = viewHolder.commentTextView;
        final TableRow hidden_layout = viewHolder.hiddenView;

        //route manipulate buttons
        ImageButton edit = viewHolder.editButton;
        ImageButton delete = viewHolder.removeButton;

        RatingBar rating = viewHolder.ratingView;
        routeName.setText(route.getName());
        date.setText(route.getDate());
        level.setText(gradeText);
        level.setTextColor(Colors.getGradeColor(gradeText));

        try {
            Drawable drawable = ContextCompat.getDrawable(viewHolder.itemView.getContext(), getRoutStyleIcon(styleText));
            style.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.e("Error drawable loading", styleText);
        }

        area.setText(Html.fromHtml(routeHtml));
        comment.setText(Html.fromHtml(commentHtml));
        rating.setRating(route.getRating());

        //show comment on holder click
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            int click = 0;

            @Override
            public void onClick(View v) {
                if (click == 0) {
                    //if last element hide add Button
                    if (mRoutes.indexOf(route) == (mRoutes.size() - 1)) {
                        AppFloatingActionButton.hide();
                    }
                    hidden_layout.setVisibility(View.VISIBLE);
                    click++;
                } else {
                    //if last element show add Button
                    if (mRoutes.indexOf(route) == (mRoutes.size() - 1)) {
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
                        boolean taskState = routeRepository.deleteRoute(route);
                        if (taskState) {
                            FragmentPager.getInstance().refreshAllFragments();
                            sDialog.hide();
                            new SweetAlertDialog(_v.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Gelöscht")
                                    .show();
                        } else {
                            AlertManager.setErrorAlert(v.getContext());
                        }

                    })
                    .setCancelButton("Cancel", SweetAlertDialog::cancel)
                    .show();
        });

        //edit a route
        edit.setOnClickListener(view -> {
            DialogFactory.openEditRouteDialog(Tabs.ROUTEN, route.getId());
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    //filter for search view
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mRoutes = mRoutes_filtered;
                } else {
                    List<Route> filteredList = new ArrayList<>();
                    for (Route row : mRoutes) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLevel().contains(charString) ||
                                row.getArea().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSector().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDate().contains(charString)
                        ) {
                            filteredList.add(row);
                        }
                    }

                    mRoutes = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mRoutes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mRoutes = (ArrayList<Route>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private int getRoutStyleIcon(String style) {
        style = style.toUpperCase();
        switch (style) {
            case "OS":
                return R.drawable.ic_os;
            case "RP":
                return R.drawable.ic_rp;
            case "FLASH":
                return R.drawable.ic_flash;
        }
        return 0;
    }
}