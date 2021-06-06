package com.main.climbingdiary.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.main.climbingdiary.R
import com.main.climbingdiary.adapter.ProjektAdapter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.removeAllFilterPrefs
import com.main.climbingdiary.common.preferences.AppPreferenceManager.setSort
import com.main.climbingdiary.controller.AppBarMenu
import com.main.climbingdiary.controller.FilterHeader
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.model.RouteSort
import java.util.*

@SuppressLint("StaticFieldLeak")
object RouteProjectFragment : Fragment(), RouteFragment {
    private lateinit var adapter: ProjektAdapter
    private lateinit var rvProjekte: RecyclerView
    private const val filter_checked = false
    private lateinit var view: View
    private lateinit var header: FilterHeader
    private val routeRepository: RouteRepository<Projekt> = RouteRepository(Projekt::class)
    private lateinit var projekts: ArrayList<Projekt>

    override fun getView(): View {
        return view
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.project_fragment, container, false)
        // Lookup the recyclerview in activity layout
        rvProjekte = view.findViewById(R.id.rvProjekte)
        removeAllFilterPrefs()
        // Initialize projects
        projekts = routeRepository.getRouteList()
        // Create adapter passing in the sample user data
        adapter = ProjektAdapter(projekts)
        // Attach the adapter to the recyclerview to populate items
        rvProjekte.adapter = adapter
        // Set layout manager to position the items
        rvProjekte.layoutManager = LinearLayoutManager(
           requireActivity().applicationContext
        )
        setHasOptionsMenu(true)
        header = FilterHeader(this)
        return view
    }

    @Synchronized
    fun getAdapter(): ProjektAdapter? {
        return adapter
    }

    override fun refreshData() {
        projekts.clear()
        projekts = routeRepository.getRouteList()
        adapter = ProjektAdapter(projekts)
        rvProjekte.adapter = adapter
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
        if (id == R.id.sort_level) {
            setSort(RouteSort.LEVEL)
            refreshData()
            return true
        } else if (id == R.id.sort_area) {
            setSort(RouteSort.AREA)
            refreshData()
            return true
        } else if (id == R.id.sort_date) {
            setSort(RouteSort.DATE)
            refreshData()
            return true
        } else if (item.groupId == R.id.filter_area + 1) {
            //Filter magic here ;-)
            header.show(item.title.toString())
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}