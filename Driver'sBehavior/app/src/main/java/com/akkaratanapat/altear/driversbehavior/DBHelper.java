package com.akkaratanapat.altear.driversbehavior;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Altear on 7/1/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "driver.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Constant.TABLE_NAME + " (" + Constant._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        Constant.COLUMN_DATE +" TEXT, " +Constant.COLUMN_ACC_X +" TEXT, " +Constant.COLUMN_ACC_Y +" TEXT, " +Constant.COLUMN_ACC_Z
                +" TEXT, " +Constant.COLUMN_GYRO_X +" TEXT, " +Constant.COLUMN_GYRO_Y +" TEXT, " +Constant.COLUMN_GYRO_Z
                +" TEXT, " +Constant.COLUMN_SPEED +" TEXT, " +Constant.COLUMN_ANGLE+" TEXT, " +Constant.COLUMN_EVENT+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
