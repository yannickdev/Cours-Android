package com.yannick.radioo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.yannick.radioo.SQLiteHelper.COLUMN_PODCAST_ID;
import static com.yannick.radioo.SQLiteHelper.TABLE_PODCASTS;

public class PodcastDAO {
    // Champs de la base de données
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_PODCAST_ID, SQLiteHelper.COLUMN_PODCAST_TITLE,SQLiteHelper.COLUMN_DURATION,SQLiteHelper.COLUMN_FILE_URL,SQLiteHelper.COLUMN_FAVICON_URL,SQLiteHelper.COLUMN_DATE
    };

    public PodcastDAO(Context context) {
        dbHelper = new SQLiteHelper(context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.DATABASE_VERSION);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(Podcast podcast){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_PODCAST_TITLE, podcast.getTitle());
        values.put(SQLiteHelper.COLUMN_DURATION, podcast.getDuration());
        values.put(SQLiteHelper.COLUMN_FILE_URL, podcast.getFileUrl());
        values.put(SQLiteHelper.COLUMN_FAVICON_URL, podcast.getFaviconUrl());
        values.put(SQLiteHelper.COLUMN_DATE, podcast.getDate().toString());
        return database.insert(SQLiteHelper.TABLE_PODCASTS, null, values);
    }

    public int update(Podcast podcast){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_PODCAST_TITLE, podcast.getTitle());
        values.put(SQLiteHelper.COLUMN_DURATION, podcast.getDuration());
        values.put(SQLiteHelper.COLUMN_FILE_URL, podcast.getFileUrl());
        values.put(SQLiteHelper.COLUMN_FAVICON_URL, podcast.getFaviconUrl());
        values.put(SQLiteHelper.COLUMN_DATE, podcast.getDate().toString());
        return database.update(TABLE_PODCASTS, values, COLUMN_PODCAST_ID + " = " +podcast.getId(), null);
    }


    public List<Podcast> getAllPodcasts() {
        List<Podcast> podcasts = new ArrayList<Podcast>();

        Cursor cursor = database.query(TABLE_PODCASTS,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();

       // if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                if (cursor.getCount() == 0)
                    return null;

                Podcast podcast = new Podcast();

                podcast.setId(cursor.getInt(0));
                podcast.setTitle(cursor.getString(1));
                podcast.setDuration(cursor.getInt(2));
                podcast.setFileUrl(cursor.getString(3));
                podcast.setFaviconUrl(cursor.getString(4));
                podcast.setDate(cursor.getString(5));

                podcasts.add(podcast);

                cursor.moveToNext();
            }
      //  }
        cursor.close();
        return podcasts;
    }

    public void deletePodcast(Podcast podcast) {
        database.delete(TABLE_PODCASTS, SQLiteHelper.COLUMN_PODCAST_ID + " = ?" , new String[] { ""+podcast.getId()});
        //database.delete(TABLE_STATIONS, SQLiteHelper.COLUMN_PODCAST_ID + " = ?" , new String[] { podcast.getId()});
        File file = new File(podcast.getFileUrl());

        if(file.delete()){
            System.out.println(file.getName() + " is deleted!");
        }else{
            System.out.println("Delete operation is failed.");
        }

    }

    private Podcast cursorToPodcast(Cursor cursor) {
        Podcast podcast = new Podcast();

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            podcast.setId(cursor.getInt(5));
            podcast.setTitle(cursor.getString(6));
            podcast.setDuration(cursor.getInt(7));
            podcast.setFileUrl(cursor.getString(8));
            podcast.setFaviconUrl(cursor.getString(9));
            podcast.setDate(cursor.getString(10));
        }

        return podcast;
    }
}
