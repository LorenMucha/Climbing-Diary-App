package com.main.climbingdiary.models

import cn.pedant.SweetAlert.SweetAlertDialog

data class Alert(
    val title: String? = null,
    val message: String? = null,
    val image: Int? = null,
    val dialogType: Int? = SweetAlertDialog.SUCCESS_TYPE
)