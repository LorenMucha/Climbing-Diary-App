package com.main.climbingdiary.provider

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.main.climbingdiary.activities.MainActivity
import com.main.climbingdiary.common.EnvironmentParamter
import com.main.climbingdiary.common.preferences.AppPreferenceManager.getOutputPath
import com.main.climbingdiary.database.TaskRepository
import java.io.*
import java.nio.channels.FileChannel


class AppFileProvider {

    private val context: Context by lazy { MainActivity.getMainAppContext() }

    @Throws(IOException::class)
    fun exportDBtoPreferencePath() {
        val path = getOutputPath()
        val currentDB = File(Environment.getDataDirectory().toString(), EnvironmentParamter.DB_PATH)
        val backupDB = File(path, EnvironmentParamter.dbExportName)
        FileInputStream(currentDB).channel.use { source ->
            FileOutputStream(backupDB, false).channel.use { destination ->
                destination.transferFrom(source, 0, source.size())
            }
        }
    }

    @Throws(IOException::class)
    fun restoreDBfromPreferencePath(uri: Uri): Boolean {
        TaskRepository.close()
        val dst = File(Environment.getDataDirectory(), EnvironmentParamter.DB_PATH)
        dst.parentFile?.mkdirs()
        File("${dst.absolutePath}-wal").delete()
        File("${dst.absolutePath}-shm").delete()
        if (dst.exists() && !dst.delete()) {
            throw IOException("Unable to delete existing database")
        }

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(dst, false).use { out ->
                inputStream.copyTo(out)
                out.flush()
            }
        } ?: throw IOException("Unable to open selected backup")

        return dst.exists() && dst.length() > 0
    }
}
