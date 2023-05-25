package com.main.climbingdiary.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.main.climbingdiary.R
import com.main.climbingdiary.adapter.RoutesAdapter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSort
import com.main.climbingdiary.controller.AppBarMenu
import com.main.climbingdiary.controller.FilterHeader
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.models.RouteSort

@SuppressLint("StaticFieldLeak", "ValidFragment")
object RouteDoneFragment : Fragment(), RouteFragment {

    private val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)
    private lateinit var rvRoutes: RecyclerView
    private lateinit var header: FilterHeader
    private lateinit var routes: ArrayList<Route>
    private lateinit var view: View
    private lateinit var adapter: RoutesAdapter

    override fun getView(): View {
        return view
    }

    fun getAdapter(): RoutesAdapter {
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.routes_fragment, container, false)
        //new FilterHeader(view);
        setHasOptionsMenu(true)
        header = FilterHeader(this)
        // Lookup the recyclerview in activity layout
        rvRoutes = view.findViewById(R.id.rvRoutes)
        // Initialize routes
        routes = routeRepository.getRouteList()
        // Create adapter passing in the sample user data
        adapter = RoutesAdapter(routes)
        // Attach the adapter to the recyclerview to populate items
        rvRoutes.adapter = adapter
        // Set layout manager to position the items
        rvRoutes.layoutManager = LinearLayoutManager(
            requireActivity().applicationContext
        )
        return view
    }

    override fun refreshData() {
        routes.clear()
        routes = routeRepository.getRouteList()
        adapter = RoutesAdapter(routes)
        rvRoutes.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater)
        val appmenu = AppBarMenu(menu)
        appmenu.setItemVisebility(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        val id = item.itemId
        when {
            id == R.id.sort_level -> {
                setSort(RouteSort.LEVEL)
                refreshData()
                return true
            }
            id == R.id.sort_area -> {
                setSort(RouteSort.AREA)
                refreshData()
                return true
            }
            id == R.id.sort_date -> {
                setSort(RouteSort.DATE)
                refreshData()
                return true
            }
            item.groupId == R.id.filter_area + 1 -> {
                header.show(item.title.toString())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}