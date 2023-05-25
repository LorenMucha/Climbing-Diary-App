package com.main.climbingdiary.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository


@SuppressLint("ValidFragment")
class AddRouteDialog(val title: String) : DialogFragment() {

    private val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)

    init {
        val args = Bundle()
        args.putString("title", title)
        this.arguments = args
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_route, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        Log.d("State set:", AppPreferenceManager.getSportType().toString())
        val creator = RouteDialogCreator(view, context, this)
        creator.setUiElements(false)

        // Fetch arguments from bundle and set title
        val title = requireArguments().getString("title", "Neue Kletterroute")
        creator.setForeGroundSpan(title)

        //set tries to 1 if os or flash
        creator.saveRoute

        //save the route
        creator.saveRoute.setOnClickListener { v: View? ->
            if (creator.checkDate()) {
                val newRoute: Route = creator.getRoute(false)
                val taskState = routeRepository.insertRoute(newRoute)
                if (taskState) {
                    refreshAllFragments()
                } else {
                    AlertFactory.getErrorAlert(view.context).show()
                }
                //close the dialog
                dialog!!.cancel()
            }
        }
    }
}
