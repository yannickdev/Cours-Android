package com.yannick.radioo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.yannick.radioo.SQLiteHelper.COLUMN_UUID;
import static com.yannick.radioo.SQLiteHelper.TABLE_SCHEDULED;

public class ScheduledStationDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { COLUMN_UUID, SQLiteHelper.COLUMN_NAME,SQLiteHelper.COLUMN_COUNTRY,SQLiteHelper.COLUMN_COUNTRY_CODE,SQLiteHelper.COLUMN_URL,
            SQLiteHelper.COLUMN_FAVICON, SQLiteHelper.COLUMN_START_TIME, SQLiteHelper.COLUMN_END_TIME, SQLiteHelper.COLUMN_STATUS
    };

    public ScheduledStationDAO(Context context) {
        dbHelper = new SQLiteHelper(context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(ScheduledStation station){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_NAME, station.getName());
        values.put(COLUMN_UUID, station.getStationuuid());
        values.put(SQLiteHelper.COLUMN_COUNTRY, station.getCountry());
        values.put(SQLiteHelper.COLUMN_COUNTRY_CODE, station.getCountrycode());
        values.put(SQLiteHelper.COLUMN_URL, station.getUrl());
        values.put(SQLiteHelper.COLUMN_FAVICON, station.getFavicon());
        values.put(SQLiteHelper.COLUMN_START_TIME, ""+station.getStartDate());
        values.put(SQLiteHelper.COLUMN_END_TIME, ""+station.getEndDate());
        values.put(SQLiteHelper.COLUMN_STATUS, ""+station.getStatus());

        return database.insert(SQLiteHelper.TABLE_SCHEDULED, null, values);
    }

    public List<ScheduledStation> getAllStations() {
        List<ScheduledStation> stations = new ArrayList<ScheduledStation>();

        Cursor cursor = database.query(TABLE_SCHEDULED,
                allColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getCount() == 0){
                    return null;
                }


                ScheduledStation station = new ScheduledStation();
                station.setStationuuid(cursor.getString( 0));
                station.setName(cursor.getString(1));
                station.setCountry(cursor.getString(2));
                station.setCountrycode(cursor.getString(3));
                station.setUrl(cursor.getString(4));
                station.setFavicon(cursor.getString(5));
                station.setStartDate(""+cursor.getString(5));
                station.setEndDate(""+cursor.getString(5));
                //station.setStatus(cursor.getString(5));


                stations.add(station);

                cursor.moveToNext();
            }
        }
        cursor.close();
        return stations;
    }

    public void deleteStation(ScheduledStation station) {
        Log.v("uuid",""+station.getStationuuid());
        database.delete(TABLE_SCHEDULED, COLUMN_UUID + " = ?" , new String[] { station.getStationuuid()});
    }

    public boolean existInDb(ScheduledStation station){
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_SCHEDULED + " WHERE " + COLUMN_UUID + " = ?" , new String[] {station.getStationuuid()});
        return cursor.getCount()!=0;
    }

    private ScheduledStation cursorToStation(Cursor cursor) {
        ScheduledStation station = new ScheduledStation();

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            station.setStationuuid(cursor.getString(0));
            station.setName(cursor.getString(1));
            station.setCountry(cursor.getString(2));
            station.setCountrycode(cursor.getString(3));
            station.setUrl(cursor.getString(4));
            station.setFavicon(cursor.getString(5));
            station.setStartDate(""+cursor.getString(6));
            station.setEndDate(""+cursor.getString(7));
            //station.setStatus(cursor.getString(5));
        }

        return station;
    }
}
