package com.main.climbingdiary.models

import cn.pedant.SweetAlert.SweetAlertDialog

data class Alert (val title: String?, val message: String?="", val dialogType: Int?=SweetAlertDialog.SUCCESS_TYPE)