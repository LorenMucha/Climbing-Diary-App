package com.main.climbingdiary.models

import cn.pedant.SweetAlert.SweetAlertDialog

class Alert constructor(val title: String?, val message: String?, val dialogType: Int?) {
    data class Builder(
        var title: String? = "",
        var message: String? = "",
        var dialogType: Int? = SweetAlertDialog.NORMAL_TYPE){
        fun title(title:String) = apply { this.title = title }
        fun message(message:String) = apply { this.message = message }
        fun dialogType(dialogType:Int) = apply { this.dialogType = dialogType }
        fun build() = Alert(title,message,dialogType)
    }
}