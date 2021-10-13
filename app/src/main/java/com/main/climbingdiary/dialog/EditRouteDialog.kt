package com.main.climbingdiary.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.common.RouteConverter.routeToProjekt
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.controller.FragmentPager.setPosition
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.models.Tabs
import java.util.concurrent.atomic.AtomicBoolean

/*
Fixme: switcher level noch anpassen
 */
@SuppressLint("ValidFragment")
class EditRouteDialog(private val title: String, private val routeId: Int) : DialogFragment() {

    private val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_route, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        val taskState = AtomicBoolean(false)
        //get the route value which will be edit
        val editRoute = routeRepository.getRoute(routeId)
        val creator = RouteDialogCreator(view, context, this)
        creator.setForeGroundSpan(title)
        creator.setUiElements(editRoute)
        creator.saveRoute.setOnClickListener {
            //save ticked Project on if else save updated Route
            if (AppPreferenceManager.getSelectedTabsTitle() === Tabs.PROJEKTE) {
                setPosition(1)
                //needs to be converted to a project
                routeRepository.deleteRoute(routeToProjekt(editRoute))
                taskState.set(routeRepository.insertRoute(creator.getRoute(true)))
            } else {
                val updateRoute = creator.getRoute(false)
                updateRoute.id = editRoute.id
                taskState.set(routeRepository.updateRoute(updateRoute))
            }
            //close the dialog
            dialog!!.cancel()
            if (taskState.get()) {
                refreshAllFragments()
            } else {
                AlertManager.setErrorAlert(view.context)
            }
        }
    }
}
