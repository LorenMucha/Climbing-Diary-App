package com.main.climbingdiary.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.FragmentPager.refreshAllFragments
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.RouteRepository

@SuppressLint("ValidFragment")
class AddProjektDialog(val title: String) : DialogFragment() {

    private val routeRepository: RouteRepository<*> = RouteRepository(Projekt::class)

    init {
        val args = Bundle()
        args.putString("title", title)
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
        val creator = RouteDialogCreator(view, context, this)
        creator.setUiElements(true)

        // Fetch arguments from bundle and set title
        if (arguments == null) throw AssertionError()
        val title = requireArguments().getString("title", "Neues Projekt")
        creator.setForeGroundSpan(title)
        creator.saveRoute.setOnClickListener { v ->
            val newProjekt: Projekt = creator.getProjekt(false) as Projekt
            val taskState = routeRepository.insertRoute(newProjekt)
            if (taskState) {
                refreshAllFragments()
            } else {
                AlertManager.setErrorAlert(view.context)
            }
            //close the dialog
            dialog!!.cancel()
        }
    }
}
