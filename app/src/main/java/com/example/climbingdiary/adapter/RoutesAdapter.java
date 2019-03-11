package com.example.climbingdiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.climbingdiary.R;
import com.example.climbingdiary.models.Colors;
import com.example.climbingdiary.models.Route;

import org.w3c.dom.Text;

import java.util.List;

public class RoutesAdapter extends
        RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private List<Route> mRoutes;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView nameTextView;
        TextView dateTextView;
        TextView levelTextView;
        TextView areaTextView;
        TextView styleTextView;
        RatingBar ratingView;
        TextView commentTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.route_name);
            dateTextView = (TextView) itemView.findViewById(R.id.route_date);
            levelTextView=(TextView) itemView.findViewById(R.id.route_level);
            areaTextView=(TextView) itemView.findViewById(R.id.route_area);
            styleTextView=(TextView) itemView.findViewById(R.id.route_style);
            ratingView = (RatingBar) itemView.findViewById(R.id.route_rating);
            commentTextView= (TextView) itemView.findViewById(R.id.route_comment);
        }
    }

    // Pass in the contact array into the constructor
    public RoutesAdapter(List<Route> routes) {
        mRoutes = routes;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_route, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RoutesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Route route = mRoutes.get(position);

        String gradeText = route.getLevel();
        String styleText= route.getStyle();
        String areaText = route.getArea();
        String sectorText = route.getSector();
        String commentString = route.getComment();

        //create the hztml string for the route and sector
        final String routeHtml ="<b>"+areaText+"</b><br/>"+sectorText;
        final String commentHtml = "<b>Kommentar</b><br/>"+commentString;

        // Set item views
        TextView routeName = viewHolder.nameTextView;
        TextView date = viewHolder.dateTextView;
        TextView level = viewHolder.levelTextView;
        TextView style = viewHolder.styleTextView;
        TextView area = viewHolder.areaTextView;
        final TextView comment = viewHolder.commentTextView;

        RatingBar rating = viewHolder.ratingView;
        routeName.setText(route.getName());
        date.setText(route.getDate());
        level.setText(gradeText);
        level.setTextColor(Colors.getGradeColor(gradeText));
        style.setText(styleText);
        style.setTextColor(Colors.getStyleColor(styleText));
        area.setText(Html.fromHtml(routeHtml));
        comment.setText(Html.fromHtml(commentHtml));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            int click = 0;
            @Override
            public void onClick(View v) {
                if(click==0){
                    comment.setVisibility(View.VISIBLE);
                    click++;
                }else{
                    comment.setVisibility(View.GONE);
                    click =0;
                }
            }
        });

        //set the on Click listener
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),Html.fromHtml(routeHtml),Toast.LENGTH_LONG).show();
            }
        });
        rating.setRating(route.getRating());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mRoutes.size();
    }
}