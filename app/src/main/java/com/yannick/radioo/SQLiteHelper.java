package com.yannick.radioo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_STATIONS = "stations";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_COUNTRY_CODE = "countrycode";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_FAVICON = "favicon";

    public static final String TABLE_PODCASTS = "podcasts";
    public static final String COLUMN_PODCAST_ID = "id";
    public static final String COLUMN_PODCAST_TITLE = "title";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_FILE_URL = "audiourl";
    public static final String COLUMN_FAVICON_URL = "faviconurl";
    public static final String COLUMN_DATE = "date";

    public static final String COLUMN_SCHEDULED_ID = "id";
    public static final String TABLE_SCHEDULED = "scheduled";
    public static final String COLUMN_START_TIME = "starttime";
    public static final String COLUMN_END_TIME = "endtime";
    public static final String COLUMN_STATUS = "status";


    private static final int NUM_UUID = 0;
    private static final int NUM_NAME = 1;
    private static final int NUM_COUNTRY = 2;
    private static final int NUM_COUNTRY_CODE = 3;
    private static final int NUM_FAVICON = 4;

    public static final int NUM_PODCAST_ID = 5;
    public static final int NUM_PODCAST_TITLE = 6;
    public static final int NUM_DURATION = 7;
    public static final int NUM_FILE_URL = 8;
    public static final int NUM_FAVICON_URL = 9;
    public static final int NUM_DATE = 10;

    public static final String DATABASE_NAME = "stations.db";
    public static final int DATABASE_VERSION = 3;

    private static final String TABLE_STATION_CREATE = "create table "+ TABLE_STATIONS
            + "("
            + COLUMN_UUID + " text not null,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_COUNTRY + " text not null,"
            + COLUMN_COUNTRY_CODE + " text not null,"
            + COLUMN_URL + " text not null,"
            + COLUMN_FAVICON + " text"
            +");";

    private static final String TABLE_PODCAST_CREATE = "create table "+ TABLE_PODCASTS
            + "("
            + COLUMN_PODCAST_ID + " integer primary key autoincrement not null, "
            + COLUMN_PODCAST_TITLE + " text not null,"
            + COLUMN_DURATION + " integer not null,"
            + COLUMN_FILE_URL + " text not null,"
            + COLUMN_FAVICON_URL + " text,"
            + COLUMN_DATE + " text not null"
            +");";

    private static final String TABLE_SCHEDULED_CREATE = "create table "+ TABLE_SCHEDULED
            + "("
            + COLUMN_SCHEDULED_ID + " integer primary key autoincrement not null, "
            + COLUMN_UUID + " text not null,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_COUNTRY + " text not null,"
            + COLUMN_COUNTRY_CODE + " text not null,"
            + COLUMN_URL + " text not null,"
            + COLUMN_FAVICON + " text,"
            + COLUMN_START_TIME + " text not null,"
            + COLUMN_END_TIME + " text not null,"
            + COLUMN_STATUS + " text not null"
            +");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_STATION_CREATE);
        database.execSQL(TABLE_PODCAST_CREATE);
        database.execSQL(TABLE_SCHEDULED_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PODCASTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULED);
        onCreate(db);
    }
}
