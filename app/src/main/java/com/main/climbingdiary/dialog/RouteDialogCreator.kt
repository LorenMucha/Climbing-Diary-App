package com.main.climbingdiary.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.main.climbingdiary.R
import com.main.climbingdiary.common.AlertManager.setAlertWithoutContent
import com.main.climbingdiary.common.GradeConverter.convertUiaaToFrench
import com.main.climbingdiary.common.RouteConverter.cleanRoute
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSelectedTabsTitle
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getSportType
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.FragmentPager.refreshSelectedFragment
import com.main.climbingdiary.controller.SetDate
import com.main.climbingdiary.database.entities.AreaRepository.getAreaNameList
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.SectorRepository.getSectorList
import com.main.climbingdiary.models.Alert
import com.main.climbingdiary.models.Levels.getLevelsFrench
import com.main.climbingdiary.models.Levels.getLevelsUiaa
import com.main.climbingdiary.models.Rating.getRating
import com.main.climbingdiary.models.SportType
import com.main.climbingdiary.models.Styles.getStyle
import com.main.climbingdiary.models.Tabs
import java.util.*
import java.util.regex.Pattern

class RouteDialogCreator(
    val view: View,
    val context: Context,
    private val fragment: DialogFragment
) {

    private var stil: Spinner = view.findViewById(R.id.input_route_stil)
    private var level: Spinner = view.findViewById(R.id.input_route_level)
    private var rating: Spinner = view.findViewById(R.id.input_route_rating)
    private var date: EditText = view.findViewById(R.id.input_route_date)
    private var name: EditText = view.findViewById(R.id.input_route_name)
    private var nameHeader: TextView = view.findViewById(R.id.input_route_name_header)
    private var area: AutoCompleteTextView = view.findViewById(R.id.input_route_area)
    private var sector: AutoCompleteTextView = view.findViewById(R.id.input_route_sektor)
    private var comment: EditText =
        view.findViewById<AutoCompleteTextView>(R.id.input_route_comment)
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var gradeSwitcher: Switch = view.findViewById(R.id.grade_system_switcher)
    private var routeContent: LinearLayout = view.findViewById(R.id.route_content)
    private var gradeSwitcherLayout: LinearLayout = view.findViewById(R.id.grade_switcher_layout)
    private val closeDialog: Button = view.findViewById(R.id.input_route_close)
    var saveRoute: Button = view.findViewById(R.id.input_route_save)

    init {
        setOnCloseButton()
    }

    @SuppressLint("SetTextI18n")
    fun setUiElements(projekt: Projekt) {
        saveRoute.text = "Update"
        name.setText(projekt.name)
        setRouteNameHeaderText()
        // set Spinner for choosing the level
        setLevelSpinner(projekt.level, getLevelsFrench())
        setGradeSwitcher(true)

        //get the route List and set autocomplete
        setAreaSectorAutoComplete()
        area.setText(projekt.area)
        sector.setText(projekt.sector)
        comment.setText(projekt.comment)
        // set the Spinner
        setRatingSpinner(projekt.rating!! - 1)
        SetDate(date, context)
        routeContent.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun setUiElements(route: Route?) {
        saveRoute.text = "Update"
        name.setText(route?.name)
        setRouteNameHeaderText()
        // set Spinner for choosing the style
        setStilSpinner(route!!.style.toUpperCase(Locale.ROOT))
        // set Spinner for choosing the level
        setLevelSpinner(route.level, getLevelsFrench())

        //get the route List and set autocomplete
        setAreaSectorAutoComplete()
        area.setText(route.area)
        sector.setText(route.sector)
        comment.setText(route.comment)
        // set the Spinner
        setRatingSpinner(route.rating!! - 1)
        SetDate(date, context)
        setGradeSwitcher(false)

        //set date listener
        try {
            SetDate(date, context)
            val sBuilder = StringBuilder()
            val dateSplit = route.date!!.split(Pattern.quote(".").toRegex()).toTypedArray()
            sBuilder.append(dateSplit[2]).append("-").append(dateSplit[1]).append("-")
                .append(dateSplit[0])
            date.setText(sBuilder.toString())
        } catch (e: Exception) {
            Log.e("Error", e.toString())
            date.setText(route.date)
        }
    }

    fun setUiElements(projekt: Boolean) {
        if (projekt) {
            routeContent.visibility = View.GONE
        }
        setStilSpinner("RP")
        setLevelSpinner("8a", getLevelsFrench())
        setAreaSectorAutoComplete()
        setRatingSpinner(1)
        setGradeSwitcher(true)
        setRouteNameHeaderText()
        SetDate(date, context)
    }

    fun setForeGroundSpan(title: String) {
        // Initialize a new foreground color span instance
        val foregroundColorSpan = ForegroundColorSpan(Color.BLACK)

        // Initialize a new spannable string builder instance
        val ssBuilder = SpannableStringBuilder(title)

        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        fragment.dialog!!.setTitle(ssBuilder)

        // Set title divider color
        val titleDividerId: Int =
            fragment.resources.getIdentifier("titleDivider", "id", "android")
        fragment.dialog!!.findViewById<View?>(titleDividerId)?.setBackgroundColor(
            fragment.resources.getColor(android.R.color.black)
        )
    }

    private fun setGradeSwitcher(visibility: Boolean) {
        val viewState =
            if (visibility && getSportType() === SportType.KLETTERN) View.VISIBLE else View.GONE
        gradeSwitcherLayout.visibility = viewState
        gradeSwitcher.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                setLevelSpinner("8a", getLevelsFrench())
            } else {
                setLevelSpinner("IX+/X-", getLevelsUiaa())
            }
        }
    }

    private fun setAreaSectorAutoComplete() {
        val areaArrayAdapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, getAreaNameList())
        //will start working from first character
        area.threshold = 1
        area.setAdapter(areaArrayAdapter)
        area.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val sectorArrayAdapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item,
                    getSectorList(area.text.toString().trim { it <= ' ' })
                )
                //will start working from first character
                sector.threshold = 1
                sector.setAdapter(sectorArrayAdapter)
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setStilSpinner(selection: String) {
        // set Spinner for choosing the style
        val stilArrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, getStyle(true))
        stilArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        stil.adapter = stilArrayAdapter
        try {
            stil.setSelection(stilArrayAdapter.getPosition(selection))
        } catch (ex: Exception) {
            Log.e("Style was unset", ex.localizedMessage)
        }
    }

    private fun setLevelSpinner(selection: String, levels: Array<String>) {
        val levelArrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, levels)
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        level.adapter = levelArrayAdapter
        level.setSelection(levelArrayAdapter.getPosition(selection))
    }

    private fun setRatingSpinner(selection: Int) {
        // set Spinner for choosing the level
        val ratingArrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, getRating())
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // The drop down view
        rating.adapter = ratingArrayAdapter
        rating.setSelection(selection)
    }

    private fun setOnCloseButton() {
        //close the dialog
        closeDialog.setOnClickListener {
            if (getSelectedTabsTitle() === Tabs.PROJEKTE) {
                refreshSelectedFragment()
            }
            fragment.dialog!!.cancel()
        }
    }

    private fun setRouteNameHeaderText() {
        val header = getSportType().getRouteName() + "name"
        nameHeader.text = header
    }

    fun getRoute(id: Boolean): Route {
        val newRoute = Route()
        if (id) {
            newRoute.id = 0
        }
        var level: String = this.level.selectedItem.toString()
        try {
            level =
                if (gradeSwitcher.isChecked) level else convertUiaaToFrench(level).toString()
        } catch (ignored: Exception) {
        }
        newRoute.name = this.name.text.toString()
        newRoute.level = level
        newRoute.date = this.date.text.toString()
        newRoute.area = this.area.text.toString()
        newRoute.sector = this.sector.text.toString()
        newRoute.comment = this.comment.text.toString()
        newRoute.rating = this.rating.selectedItemPosition + 1
        newRoute.style = this.stil.selectedItem.toString()
        return cleanRoute(newRoute) as Route
    }

    fun getProjekt(id: Boolean): Projekt {
        val projekt = Projekt()
        if (id) {
            projekt.id = 0
        }
        var level: String = this.level.selectedItem.toString()
        try {
            level =
                if (gradeSwitcher.isChecked) level else convertUiaaToFrench(level).toString()
        } catch (ignored: Exception) {
        }
        projekt.name = this.name.text.toString()
        projekt.level = level
        projekt.area = this.area.text.toString()
        projekt.sector = this.sector.text.toString()
        projekt.comment = this.comment.text.toString()
        projekt.rating = this.rating.selectedItemPosition + 1
        return cleanRoute(projekt) as Projekt
    }

    fun checkDate(): Boolean {
        return if (date.text.toString().trim { it <= ' ' }.isEmpty()) {
            val alert = Alert("Datum fehlt \ud83d\ude13", "", SweetAlertDialog.ERROR_TYPE)
            setAlertWithoutContent(context, alert)
            false
        } else {
            true
        }
    }

}