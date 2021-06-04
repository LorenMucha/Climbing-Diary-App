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
import com.main.climbingdiary.common.AppPreferenceManager
import com.main.climbingdiary.common.GradeConverter
import com.main.climbingdiary.common.RouteConverter
import com.main.climbingdiary.common.preferences.AlertManager
import com.main.climbingdiary.controller.FragmentPager
import com.main.climbingdiary.controller.SetDate
import com.main.climbingdiary.database.entities.AreaRepository
import com.main.climbingdiary.database.entities.Projekt
import com.main.climbingdiary.database.entities.Route
import com.main.climbingdiary.database.entities.SectorRepository
import com.main.climbingdiary.model.*
import java.util.*
import java.util.regex.Pattern
import android.R as AR

class RouteDialogCreator(_view: View, context: Context, _fragment: DialogFragment) {

    var closeDialog: Button
    var saveRoute: Button
    private val stil: Spinner
    private val level: Spinner
    private val rating: Spinner
    private val date: EditText
    private val name: EditText
    private val nameHeader: TextView
    private val area: AutoCompleteTextView
    private val sector: AutoCompleteTextView
    private val comment: EditText
    private val _context: Context
    private val view: View = _view

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private val gradeSwitcher: Switch
    private val fragment: DialogFragment
    private val routeContent: LinearLayout
    private val gradeSwitcherLayout: LinearLayout

    init {
        closeDialog = view.findViewById(R.id.input_route_close)
        saveRoute = view.findViewById(R.id.input_route_save)
        stil = view.findViewById(R.id.input_route_stil)
        level = view.findViewById(R.id.input_route_level)
        rating = view.findViewById(R.id.input_route_rating)
        date = view.findViewById(R.id.input_route_date)
        name = view.findViewById(R.id.input_route_name)
        nameHeader = view.findViewById(R.id.input_route_name_header)
        area = view.findViewById(R.id.input_route_area)
        sector = view.findViewById(R.id.input_route_sektor)
        comment = view.findViewById(R.id.input_route_comment)
        gradeSwitcher = view.findViewById(R.id.grade_system_switcher)
        routeContent = view.findViewById(R.id.route_content)
        gradeSwitcherLayout = view.findViewById(R.id.grade_switcher_layout)
        fragment = _fragment
        _context = context
        setOnCloseButton()
    }

    @SuppressLint("SetTextI18n")
    fun setUiElements(projekt: Projekt) {
        saveRoute.text = "Update"
        name.setText(projekt.name)
        setRouteNameHeaderText()
        // set Spinner for choosing the level
        setLevelSpinner(projekt.level, Levels.getLevelsFrench())
        setGradeSwitcher(true)

        //get the route List and set autocomplete
        setAreaSectorAutoComplete()
        area.setText(projekt.area)
        sector.setText(projekt.sector)
        comment.setText(projekt.comment)
        // set the Spinner
        setRatingSpinner(projekt.rating!!.minus(1))
        SetDate(date, _context)
        routeContent.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun setUiElements(route: Route) {
        saveRoute.text = "Update"
        name.setText(route.name)
        setRouteNameHeaderText()
        // set Spinner for choosing the style
        setStilSpinner(route.style.toUpperCase(Locale.ROOT))
        // set Spinner for choosing the level
        setLevelSpinner(route.level, Levels.getLevelsFrench())

        //get the route List and set autocomplete
        setAreaSectorAutoComplete()
        area.setText(route.area)
        sector.setText(route.sector)
        comment.setText(route.comment)
        // set the Spinner
        setRatingSpinner(route.rating!!.minus(1))
        SetDate(date, _context)
        setGradeSwitcher(false)

        //set date listener
        try {
            SetDate(date, _context)
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
        setLevelSpinner("8a", Levels.getLevelsFrench())
        setAreaSectorAutoComplete()
        setRatingSpinner(1)
        setGradeSwitcher(true)
        setRouteNameHeaderText()
        SetDate(date, _context)
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
        val titleDivider: View = fragment.dialog!!.findViewById(titleDividerId)
        titleDivider.setBackgroundColor(
            fragment.resources.getColor(R.color.black)
        )
    }

    private fun setGradeSwitcher(visibility: Boolean) {
        val viewState =
            if (visibility && AppPreferenceManager.getSportType() === SportType.KLETTERN) View.VISIBLE else View.GONE
        gradeSwitcherLayout.visibility = viewState
        gradeSwitcher.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                setLevelSpinner("8a", Levels.getLevelsFrench())
            } else {
                setLevelSpinner("IX+/X-", Levels.getLevelsUiaa())
            }
        }
    }

    private fun setAreaSectorAutoComplete() {
        val areaArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            view.context,
            AR.layout.simple_spinner_item,
            AreaRepository.getAreaNameList()
        )
        //will start working from first character
        area.threshold = 1
        area.setAdapter(areaArrayAdapter)
        area.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val sectorArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    _context,
                    AR.layout.simple_spinner_item,
                    SectorRepository.getSectorList(
                        area.text.toString().trim { it <= ' ' })
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
        val stilArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(_context, AR.layout.simple_spinner_item, Styles.getStyle(true))
        stilArrayAdapter.setDropDownViewResource(AR.layout.simple_spinner_dropdown_item) // The drop down view
        stil.adapter = stilArrayAdapter
        try {
            stil.setSelection(stilArrayAdapter.getPosition(selection))
        } catch (ex: Exception) {
            Log.e("Style was unset", ex.localizedMessage)
        }
    }

    private fun setLevelSpinner(selection: String, levels: Array<String>) {
        val levelArrayAdapter = ArrayAdapter(_context, AR.layout.simple_spinner_item, levels)
        levelArrayAdapter.setDropDownViewResource(AR.layout.simple_spinner_dropdown_item) // The drop down view
        level.adapter = levelArrayAdapter
        level.setSelection(levelArrayAdapter.getPosition(selection))
    }

    private fun setRatingSpinner(selection: Int) {
        // set Spinner for choosing the level
        val ratingArrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(_context, AR.layout.simple_spinner_item, Rating.getRating())
        ratingArrayAdapter.setDropDownViewResource(AR.layout.simple_spinner_dropdown_item) // The drop down view
        rating.adapter = ratingArrayAdapter
        rating.setSelection(selection)
    }

    private fun setOnCloseButton() {
        //close the dialog
        closeDialog.setOnClickListener {
            if (AppPreferenceManager.getSelectedTabsTitle() === Tabs.PROJEKTE) {
                FragmentPager.refreshSelectedFragment()
            }
            fragment.dialog!!.cancel()
        }
    }

    private fun setRouteNameHeaderText() {
        val header: String = AppPreferenceManager.getSportType().name + "name"
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
                if (gradeSwitcher.isChecked) level else GradeConverter.convertUiaaToFrench(level)
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
        return RouteConverter.cleanRoute(newRoute) as Route
    }

    fun getProjekt(id: Boolean): Projekt {
        val projekt = Projekt()
        if (id) {
            projekt.id = 0
        }
        var level: String = this.level.selectedItem.toString()
        try {
            level =
                if (gradeSwitcher.isChecked) level else GradeConverter.convertUiaaToFrench(level)
        } catch (ignored: Exception) {
        }
        projekt.name = this.name.text.toString()
        projekt.level =level
        projekt.area =this.area.text.toString()
        projekt.sector =this.sector.text.toString()
        projekt.comment = this.comment.text.toString()
        projekt.rating= this.rating.selectedItemPosition + 1
        return RouteConverter.cleanRoute(projekt) as Projekt
    }

    fun checkDate(): Boolean {
        return if (date.text.toString().trim { it <= ' ' }.isEmpty()) {
            val alert = Alert(String.format("Datum fehlt %s", "\ud83d\ude13"),"",SweetAlertDialog.ERROR_TYPE)
            AlertManager.setAlertWithoutContent(_context, alert)
            false
        } else {
            true
        }
    }
}
