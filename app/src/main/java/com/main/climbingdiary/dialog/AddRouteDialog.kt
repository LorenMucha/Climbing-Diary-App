package com.main.climbingdiary.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.TimeSlider
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository

class AddRouteDialog(val title: String) : DialogFragment() {

    private val routeRepository: RouteRepository<*> = RouteRepository(Route::class)

    init {
        val args = Bundle()
        args.putString("title", title)
        this.arguments = args
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        Log.d("State set:", AppPreferenceManager.getSportType().toString())
        val creator = RouteDialogCreator(view, context, this)
        creator.setUiElements(false)

        // Fetch arguments from bundle and set title
        val title = requireArguments().getString("title", "Neue Kletterroute")
        creator.setForeGroundSpan(title)

        //save the route
        creator.saveRoute.setOnClickListener { v: View? ->
            if (creator.checkDate()) {
                val newRoute: Route = creator.getRoute(false)
                val taskState = routeRepository.insertRoute(newRoute)
                if (taskState) {
                    FragmentPager.refreshAllFragments()
                } else {
                    AlertManager.setErrorAlert(view.context)
                }
                TimeSlider.setTimes()
                //close the dialog
                dialog!!.cancel()
            }
        }
    }
}
