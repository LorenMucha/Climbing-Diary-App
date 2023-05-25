package com.main.climbingdiary.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.StringManager
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.controller.button.AppFloatingActionButton
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.dialog.DialogFactory
import com.main.climbingdiary.models.Colors
import com.main.climbingdiary.models.Tabs
import java.util.*


class RoutesAdapter(routes: List<Route>) : Filterable,
    RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var mRoutes: List<Route> = routes
    private val mroutesFiltered: List<Route> = routes
    private val routeRepository: RouteRepository<*> = RouteRepository(Route::class)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_route, parent, false)
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val route = mRoutes[position]
        val gradeText = route.level
        val styleText = route.style
        val areaText = route.area
        val sectorText = route.sector
        val commentString = route.comment
        val tries = route.tries

        //create the html string for the route and sector
        val routeHtml = getRouteAndSectorString(areaText!!, sectorText!!)
        val commentHtml = "<b>${StringManager.getStringForId(R.string.route_item_comment)}</b><br/>$commentString"

        // Set item views
        val routeName = viewHolder.nameTextView
        val date = viewHolder.dateTextView
        val level = viewHolder.levelTextView
        val style = viewHolder.styleTextView
        val area = viewHolder.areaTextView
        val comment = viewHolder.commentTextView
        val hidden_layout = viewHolder.hiddenView
        val routeTries = viewHolder.triesTextView

        //route manipulate buttons
        val edit = viewHolder.editButton
        val delete = viewHolder.removeButton
        val rating = viewHolder.ratingView
        routeName.text = route.name
        date.text = route.date
        level.text = gradeText
        level.setTextColor(Colors.getGradeColor(gradeText))
        routeTries.text = "${route.tries.toString()} ${StringManager.getStringForId(R.string.dialog_tries_header)}"
        routeTries.also { it.visibility = if(route.tries == null) View.INVISIBLE else View.VISIBLE }

        try {
            val drawable: Drawable? =
                ContextCompat.getDrawable(viewHolder.itemView.context, getRoutStyleIcon(styleText))
            style.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("Error drawable loading", styleText)
        }
        area.text = Html.fromHtml(routeHtml, 0)
        comment.text = Html.fromHtml(commentHtml, 0)
        rating.rating = route.rating!!.toFloat()

        //show comment on holder click
        viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
            var click = 0
            override fun onClick(v: View) {
                if (click == 0) {
                    //if last element hide add Button
                    if (mRoutes.indexOf(route) == mRoutes.size - 1) {
                        AppFloatingActionButton.hide()
                    }
                    hidden_layout.visibility = View.VISIBLE
                    click++
                } else {
                    //if last element show add Button
                    if (mRoutes.indexOf(route) == mRoutes.size - 1) {
                        AppFloatingActionButton.show()
                    }
                    hidden_layout.visibility = View.GONE
                    click = 0
                }
            }
        })

        //delete a route
        delete.setOnClickListener { v: View ->
            SweetAlertDialog(v.context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(StringManager.getStringForId(R.string.route_item_dialog_delete))
                .setConfirmText(StringManager.getStringForId(R.string.app_ok))
                .setCancelText(StringManager.getStringForId(R.string.app_cancel))
                .setConfirmClickListener { sDialog: SweetAlertDialog ->
                    //delete the route by id
                    val taskState = routeRepository.deleteRoute(route)
                    if (taskState) {
                        refreshAllFragments()
                        sDialog.hide()
                        SweetAlertDialog(v.context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(StringManager.getStringForId(R.string.app_deleted))
                            .show()
                    } else {
                        AlertFactory.getErrorAlert(v.context).show()
                    }
                }
                .setCancelButton(
                    StringManager.getStringForId(R.string.app_cancel)
                ) { obj: SweetAlertDialog -> obj.cancel() }
                .show()
        }

        //edit a route
        edit.setOnClickListener {
            DialogFactory.openEditRouteDialog(
                Tabs.ROUTEN,
                route.id
            )
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mRoutes.size
    }

    //filter for search view
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mRoutes = if (charString.isEmpty()) {
                    mroutesFiltered
                } else {
                    val filteredList: MutableList<Route> = ArrayList()
                    for (row in mRoutes) {
                        if (row.name!!.lowercase(Locale.ROOT).contains(
                                charString.lowercase(
                                    Locale.ROOT
                                )
                            ) ||
                            row.level.contains(charString) ||
                            row.area!!.lowercase(Locale.ROOT).contains(
                                charString.lowercase(
                                    Locale.ROOT
                                )
                            ) ||
                            row.sector!!.lowercase(Locale.ROOT).contains(
                                charString.lowercase(
                                    Locale.ROOT
                                )
                            ) ||
                            row.date!!.contains(charString)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mRoutes
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mRoutes = filterResults.values as ArrayList<Route>
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun getRoutStyleIcon(style: String): Int {
            style.uppercase(Locale.ROOT)
            when (style) {
                "OS" -> return R.drawable.ic_os
                "RP" -> return R.drawable.ic_rp
                "FLASH" -> return R.drawable.ic_flash
            }
            return 0
        }

        fun getRouteAndSectorString(areaText: String, sectorText: String): String {
            return "$areaText &#9679; $sectorText"
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        var nameTextView: TextView = itemView.findViewById(R.id.route_name)
        var dateTextView: TextView = itemView.findViewById(R.id.route_date)
        var levelTextView: TextView = itemView.findViewById(R.id.route_level)
        var areaTextView: TextView = itemView.findViewById(R.id.route_area)
        var styleTextView: ImageView = itemView.findViewById(R.id.route_style)
        var ratingView: RatingBar = itemView.findViewById(R.id.route_rating)
        var commentTextView: TextView = itemView.findViewById(R.id.route_comment)
        var hiddenView: TableRow = itemView.findViewById(R.id.route_hidden)
        var editButton: ImageButton = itemView.findViewById(R.id.route_edit)
        var removeButton: ImageButton = itemView.findViewById(R.id.route_delete)
        var triesTextView: TextView = itemView.findViewById(R.id.route_tries)
    }
}