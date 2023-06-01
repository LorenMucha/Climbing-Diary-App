package com.main.climbingdiary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.main.climbingdiary.common.EnvironmentParamter
import com.main.climbingdiary.common.preferences.AppPreferenceManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(
        context,
        EnvironmentParamter.DB_NAME,
        factory,
        EnvironmentParamter.DB_VERSION
    ) {

    private val dbName: String = EnvironmentParamter.DB_NAME
    private val dbPath = context.applicationInfo.dataDir + "/databases/"
    private val initScript =
        context.assets.open("update.sql").bufferedReader().use { it.readText() }

    @Volatile
    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false
    private val mContext = context

    init {
        copyDataBase()
        this.readableDatabase
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(dbPath + dbName)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            this.readableDatabase
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    private fun copyDBFile() {
        val mInput = mContext.assets.open(dbName)
        val mOutput: OutputStream = FileOutputStream(dbPath + dbName)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) mOutput.write(mBuffer, 0, mLength)
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    fun openDataBase() {
        mDataBase =
            SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.CREATE_IF_NECESSARY)
    }

    private fun updateDb(){
        try {
            val db = this.writableDatabase
            initScript.split(";").toTypedArray().forEach { db.execSQL(it) }
        } catch (_: Exception) {
        }
    }

    @Synchronized
    override fun close() {
        if (mDataBase != null) mDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) mNeedUpdate = true
    }
}