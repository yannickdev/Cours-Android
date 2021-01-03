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
import static com.yannick.radioo.SQLiteHelper.TABLE_STATIONS;
import static com.yannick.radioo.SQLiteHelper.COLUMN_UUID;

public class StationDAO {
    // Champs de la base de données
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { COLUMN_UUID, SQLiteHelper.COLUMN_NAME,SQLiteHelper.COLUMN_COUNTRY,SQLiteHelper.COLUMN_COUNTRY_CODE,SQLiteHelper.COLUMN_URL,SQLiteHelper.COLUMN_FAVICON
            };

    public StationDAO(Context context) {
        dbHelper = new SQLiteHelper(context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(Favourite station){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_NAME, station.getName());
        values.put(COLUMN_UUID, station.getStationuuid());
        values.put(SQLiteHelper.COLUMN_COUNTRY, station.getCountry());
        values.put(SQLiteHelper.COLUMN_COUNTRY_CODE, station.getCountrycode());
        values.put(SQLiteHelper.COLUMN_URL, station.getUrl());
        values.put(SQLiteHelper.COLUMN_FAVICON, station.getFavicon());

        return database.insert(SQLiteHelper.TABLE_STATIONS, null, values);
    }

    public List<Favourite> getAllStations() {
        List<Favourite> stations = new ArrayList<Favourite>();

        Cursor cursor = database.query(TABLE_STATIONS,
                allColumns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if (cursor.getCount() == 0)
                    return null;

                Favourite station = new Favourite();
                station.setStationuuid(cursor.getString(0));
                station.setName(cursor.getString(1));
                station.setCountry(cursor.getString(2));
                station.setCountrycode(cursor.getString(3));
                station.setUrl(cursor.getString(4));
                station.setFavicon(cursor.getString(5));

                stations.add(station);

                cursor.moveToNext();
            }
        }
        cursor.close();
        return stations;
    }

    public void deleteStation(Favourite station) {
        Log.v("uuid",""+station.getStationuuid());
        database.delete(TABLE_STATIONS, COLUMN_UUID + " = ?" , new String[] { station.getStationuuid()});
    }

    public boolean existInDb(Favourite station){
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_STATIONS + " WHERE " + COLUMN_UUID + " = ?" , new String[] {station.getStationuuid()});
        return cursor.getCount()!=0;
    }

    private Station cursorToStation(Cursor cursor) {
        Station station = new Station();

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            station.setStationuuid(cursor.getString(0));
            station.setName(cursor.getString(1));
            station.setCountry(cursor.getString(2));
            station.setCountrycode(cursor.getString(3));
            station.setUrl(cursor.getString(4));
            station.setFavicon(cursor.getString(5));
        }

        return station;
    }
}
