package com.main.climbingdiary.common

import android.content.Context
import com.main.climbingdiary.activities.MainActivity
import java.time.LocalDate

object EnvironmentParamter {
    private val context: Context by lazy { MainActivity.getMainAppContext() }
    const val DB_VERSION = 1
    const val DB_NAME = "touren.db"
    private const val DB_NAME_COPY = "touren_copy.db"
    val dbExportName =
        "${DB_NAME.replace(".db", "")}_${LocalDate.now().toString().replace("/", "_")}.db"
    private val PACKAGE_NAME: String = context.packageName
    val DB_PATH = "/data/$PACKAGE_NAME/databases/$DB_NAME"
    val DB_PATH_COPY = "/data/${PACKAGE_NAME}/databases/${DB_NAME_COPY}"
}
