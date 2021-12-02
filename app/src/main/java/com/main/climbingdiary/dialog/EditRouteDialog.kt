package com.main.climbingdiary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.common.RouteConverter.projektToRoute
import com.main.climbingdiary.common.RouteConverter.routeToProjekt
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.controller.FragmentPager.setPosition
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.models.Alert
import com.skyfishjy.library.RippleBackground
import java.util.concurrent.atomic.AtomicBoolean

class EditRouteDialog(
    private val title: String,
    private val routeId: Int,
    private val isTickedRoute: Boolean = false
) : DialogFragment() {

    private val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)
    private val projektRepository: RouteRepository<Projekt> = RouteRepository(Projekt::class)
    private lateinit var rippleBackground: RippleBackground

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
        val editRoute = getRoute()
        val creator = RouteDialogCreator(view, context, this)
        creator.setForeGroundSpan(title)
        creator.setUiElements(editRoute, isTickedRoute)
        creator.saveRoute.setOnClickListener {
            //save ticked Project on if else save updated Route
            it.run {
                if (isTickedRoute) {
                    setPosition(1)
                    projektRepository.deleteRoute(routeToProjekt(editRoute))
                    taskState.set(routeRepository.insertRoute(creator.getRoute(true)))
                } else {
                    val updateRoute = creator.getRoute(false)
                    updateRoute.id = editRoute.id
                    taskState.set(routeRepository.updateRoute(updateRoute))
                }
                dialog!!.cancel()
                if (isTickedRoute) AlertManager.setAlert(
                    view.context, Alert(title="Stark! \uD83C\uDF89"))
                if (taskState.get()) {
                    refreshAllFragments()

                } else {
                    AlertManager.setErrorAlert(view.context)
                }
            }
        }
    }

    private fun getRoute(): Route {
        return if (isTickedRoute) projektToRoute(projektRepository.getRoute(routeId)) else routeRepository.getRoute(
            routeId
        )
    }
}
