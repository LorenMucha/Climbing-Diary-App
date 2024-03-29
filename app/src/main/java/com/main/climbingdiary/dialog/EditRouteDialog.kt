package com.main.climbingdiary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertFactory
import com.main.climbingdiary.common.RouteConverter.projektToRoute
import com.main.climbingdiary.common.RouteConverter.routeToProjekt
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.controller.FragmentPager.setPosition
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.RouteRepository
import com.main.climbingdiary.models.Alert
import java.util.concurrent.atomic.AtomicBoolean

class EditRouteDialog(
    private val title: String,
    private val routeId: Int,
    private val isTickedRoute: Boolean = false
) : DialogFragment() {

    private val routeRepository: RouteRepository<Route> = RouteRepository(Route::class)
    private val projektRepository: RouteRepository<Projekt> = RouteRepository(Projekt::class)

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
                    val routeCreated = creator.getRoute(true)
                    taskState.set(routeRepository.insertRoute(routeCreated))
                    AlertFactory.getAlert(
                        context, Alert(
                            title = "Stark!",
                            image = R.drawable.muscle,
                            dialogType = SweetAlertDialog.CUSTOM_IMAGE_TYPE
                        )
                    ).show()
                } else {
                    val updateRoute = creator.getRoute(false)
                    updateRoute.id = editRoute.id
                    taskState.set(routeRepository.updateRoute(updateRoute))
                }
                dialog!!.cancel()
                if (taskState.get()) {
                    refreshAllFragments()

                } else {
                    AlertFactory.getErrorAlert(view.context).show()
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
