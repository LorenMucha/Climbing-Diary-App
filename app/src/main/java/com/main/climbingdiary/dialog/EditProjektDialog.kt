package com.main.climbingdiary.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.main.climbingdiary.R
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository

@SuppressLint("ValidFragment")
class EditProjektDialog(title: String, _id: Int) : DialogFragment() {

    private val routeRepository: RouteRepository<Projekt> = RouteRepository(Projekt::class)

    init {
        val args = Bundle()
        args.putString("title", title)
        args.putInt("id", _id)
        this.arguments = args
    }

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
        val title: String = requireArguments().getString("title", "Bearbeiten")
        //get the route value which will be edit
        val routeId: Int = requireArguments().getInt("id", 0)
        val editProjekt = routeRepository.getRoute(routeId)
        val creator = RouteDialogCreator(view, context, this)
        creator.setForeGroundSpan(title)
        creator.setUiElements(editProjekt)

        //save the route
        creator.saveRoute.setOnClickListener {
            val projekt = creator.getProjekt(true)
            val taskState = routeRepository.deleteRoute(editProjekt)
            if (taskState) {
                routeRepository.insertRoute(projekt)
            }
            //close the dialog
            dialog!!.cancel()
            refreshAllFragments()
        }
    }
}

