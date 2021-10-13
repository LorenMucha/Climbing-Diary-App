package com.main.climbingdiary.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.button.AppFloatingActionButton
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.dialog.DialogFactory
import com.main.climbingdiary.models.Colors
import com.main.climbingdiary.models.Tabs
import java.text.SimpleDateFormat
import java.util.*

class ProjektAdapter(projekts: List<Projekt>) : Filterable,
    RecyclerView.Adapter<ProjektAdapter.ViewHolder>() {

    var mProjekts: List<Projekt>
    val mprojektsFiltered: List<Projekt>
    private val routeRepository: RouteRepository<Projekt>

    init {
        this.mProjekts = projekts
        this.mprojektsFiltered = projekts
        this.routeRepository = RouteRepository(Projekt::class)
    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val projectView = inflater.inflate(R.layout.item_projekt, parent, false)

        // Return a new holder instance
        return ViewHolder(projectView)
    }

    // Involves populating data into the item through holder
    @SuppressLint("WeekBasedYear")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val projekt = mProjekts[position]
        val gradeText = projekt.level
        val areaText = projekt.area
        val sectorText = projekt.sector
        val commentString = projekt.comment

        //create the html string for the route and sector
        val routeHtml = "$areaText &#9679; $sectorText"
        val commentHtml = "<b>Kommentar</b><br/>$commentString"

        // Set item views
        val routeName: TextView = viewHolder.nameTextView
        val level: TextView = viewHolder.levelTextView
        val area: TextView = viewHolder.areaTextView
        val comment: TextView = viewHolder.commentTextView
        val hiddenLayout: TableRow = viewHolder.hiddenView

        //route manipulate buttons
        val edit: ImageButton = viewHolder.editButton
        val delete: ImageButton = viewHolder.removeButton
        val routeTick: CheckBox = viewHolder.checkProject
        val rating: RatingBar = viewHolder.ratingView
        routeName.text = projekt.name
        level.text = gradeText
        level.setTextColor(Colors.getGradeColor(gradeText))
        area.text = Html.fromHtml(routeHtml)
        comment.text = Html.fromHtml(commentHtml)
        rating.rating = projekt.rating!!.toFloat()

        //show comment on holder click
        viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
            var click = 0
            override fun onClick(v: View) {
                if (click == 0) {
                    //if last element hide add Button
                    if (mProjekts.indexOf(projekt) == mProjekts.size - 1) {
                        AppFloatingActionButton.hide()
                    }
                    hiddenLayout.visibility = View.VISIBLE
                    click++
                } else {
                    //if last element show add Button
                    if (mProjekts.indexOf(projekt) == mProjekts.size - 1) {
                        AppFloatingActionButton.show()
                    }
                    hiddenLayout.visibility = View.GONE
                    click = 0
                }
            }
        })

        //delete a route
        delete.setOnClickListener { v: View ->
            SweetAlertDialog(v.context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Bist du sicher ?")
                .setConfirmText("OK")
                .setCancelText("Abbrechen")
                .setConfirmClickListener { sDialog: SweetAlertDialog ->
                    //delete the route by id
                    val taskState = routeRepository.deleteRoute(projekt)
                    if (taskState) {
                        refreshSelectedFragment()
                        sDialog.hide()
                        SweetAlertDialog(v.context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Gelöscht")
                            .show()
                    } else {
                        AlertManager.setErrorAlert(v.context)
                    }
                }
                .setCancelButton(
                    "Cancel"
                ) { sDialog: SweetAlertDialog -> sDialog.cancel() }
                .show()
        }
        //edit a route
        edit.setOnClickListener { view: View? ->
            DialogFactory.openEditRouteDialog(
                Tabs.PROJEKTE,
                projekt.id
            )
        }

        //tick projekt
        routeTick.setOnClickListener {
            val sdf =
                SimpleDateFormat("YYYY-MM-dd", Locale.GERMAN)
            val route = Route()
            route.id = projekt.id
            route.name = projekt.name
            route.area = projekt.area
            route.level = projekt.level
            route.sector = projekt.sector
            route.date = sdf.format(Date())
            route.rating = projekt.rating!!
            route.comment = projekt.comment
            route.style = "rp"
            Log.d("Tick projekt: ", projekt.toString())
            DialogFactory.openEditRouteDialog(route)
        }
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mProjekts.size
    }

    //filter for search view
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mProjekts = if (charString.isEmpty()) {
                    mprojektsFiltered
                } else {
                    val filteredList: MutableList<Projekt> = ArrayList()
                    for (row in mProjekts) {
                        if (row.name!!.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT)) ||
                            row.level.contains(charString) ||
                            row.area!!.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT)) ||
                            row.sector!!.toLowerCase(Locale.ROOT).contains(
                                charString.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mProjekts
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mProjekts = filterResults.values as ArrayList<Projekt>
                notifyDataSetChanged()
            }
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        var nameTextView: TextView = itemView.findViewById(R.id.route_name)
        var levelTextView: TextView = itemView.findViewById(R.id.route_level)
        var areaTextView: TextView = itemView.findViewById(R.id.route_area)
        var ratingView: RatingBar = itemView.findViewById(R.id.route_rating)
        var commentTextView: TextView = itemView.findViewById(R.id.route_comment)
        var hiddenView: TableRow = itemView.findViewById(R.id.route_hidden)
        var editButton: ImageButton = itemView.findViewById(R.id.route_edit)
        var removeButton: ImageButton = itemView.findViewById(R.id.route_delete)
        var checkProject: CheckBox = itemView.findViewById(R.id.tick_project)
    }
}