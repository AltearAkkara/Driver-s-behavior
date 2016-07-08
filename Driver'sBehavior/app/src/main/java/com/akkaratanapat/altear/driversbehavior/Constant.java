package com.akkaratanapat.altear.driversbehavior;

import android.provider.BaseColumns;

/**
 * Created by Altear on 7/1/2016.
 */
public interface Constant extends BaseColumns {

    public static final String TABLE_NAME = "event_driver";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_ACC_X = "accx";
    public static final String COLUMN_ACC_Y = "accy";
    public static final String COLUMN_ACC_Z = "accz";
    public static final String COLUMN_GYRO_X = "gyrox";
    public static final String COLUMN_GYRO_Y = "gyroy";
    public static final String COLUMN_GYRO_Z = "gyroz";
    public static final String COLUMN_ANGLE = "angle";
    public static final String COLUMN_EVENT = "event";
}
