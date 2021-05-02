package com.main.climbingdiary.common

import android.os.Build
import androidx.annotation.RequiresApi
import com.main.climbingdiary.activities.MainActivity
import java.time.LocalDate

object EnvironmentParamter {
    var DB_NAME = "touren.db"
    var DB_NAME_COPY = "touren_copy.db"

    @RequiresApi(Build.VERSION_CODES.O)
    var dbExportName = String.format(
        "%s_%s.db",
        DB_NAME.replace(".db", ""),
        LocalDate.now().toString().replace("/", "_")
    )
    var PACKAGE_NAME: String = MainActivity.getAppContext()!!.packageName
    var DB_PATH = "/data/$PACKAGE_NAME/databases/$DB_NAME"
    var DB_PATH_COPY = "/data/$PACKAGE_NAME/databases/$DB_NAME_COPY"
}