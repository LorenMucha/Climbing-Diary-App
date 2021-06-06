package com.main.climbingdiary.common

import android.net.Uri
import android.os.Environment
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.EnvironmentParamter.dbExportName
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import java.io.*
import java.nio.channels.FileChannel

class AppFileProvider {

    @Throws(IOException::class)
    fun exportDBtoPreferencePath() {
        val path = getOutputPath()
        val source: FileChannel
        val destination: FileChannel
        val currentDB = File(Environment.getDataDirectory(), EnvironmentParamter.DB_PATH)
        val backupDB = File(path, dbExportName)
        source = FileInputStream(currentDB).channel
        destination = FileOutputStream(backupDB, true).channel
        destination.transferFrom(source, 0, source.size())
        source.close()
        destination.close()
    }

    @Throws(IOException::class)
    fun restoreDBfromPreferencePath(uri: Uri): Boolean {
        val dst = File(Environment.getDataDirectory(), EnvironmentParamter.DB_PATH)
        if (dst.delete()) {
            val `in`: InputStream =
                MainActivity.getAppContext()!!.contentResolver.openInputStream(uri)!!
            val out: OutputStream = FileOutputStream(dst, false)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            out.flush()
            `in`.close()
            out.close()
            return true
        }
        return false
    }
}