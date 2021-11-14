package com.main.climbingdiary.fragments

import android.view.View

interface RouteFragment {
    fun refreshData()
    fun getView(): View
}