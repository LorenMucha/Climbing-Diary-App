package com.main.climbingdiary.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.main.climbingdiary.activities.MainActivity;
import com.main.climbingdiary.common.AlertManager;
import com.main.climbingdiary.common.AppPermissions;
import com.main.climbingdiary.common.EnvironmentParamter;
import com.main.climbingdiary.database.sql.SqlCreateTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import lombok.var;

import static android.app.Activity.RESULT_OK;
import static com.main.climbingdiary.common.preferences.PreferenceKeys.FILE_CHOOOSER_REQUEST_SAFTY_COPY;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = EnvironmentParamter.DB_NAME;
    private volatile String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    @SuppressLint("StaticFieldLeak")
    public static DatabaseHelper INSTANZ = null;

    public static synchronized DatabaseHelper getInstance(){
     if(INSTANZ == null)   {
         INSTANZ = new DatabaseHelper(MainActivity.getMainAppContext());
     }
     return INSTANZ;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    private void copyDataBase() {
        var dbExists = new File(getDbPath()).exists();
        if (!dbExists) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                AlertManager.setErrorAlert(mContext);
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(getDbPath());
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public void openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(getDbPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private String getDbPath(){
        return DB_PATH + DB_NAME;
    }
}