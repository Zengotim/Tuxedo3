package com.tk_squared.tuxedo3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

/**
* Created by Kevin on 1/10/2016.
*/
public class tkkStationsDataSource {
    public class MySQLiteHelper extends SQLiteOpenHelper {

        //  public static final String TABLE_COMMENTS = "comments";
        public static final String TABLE_STATIONS = "stations";
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IDX = "idx";
        //  public static final String COLUMN_COMMENT = "comment";

        private static final String DATABASE_NAME = "stations.db";
        private static final int DATABASE_VERSION = 1;

        // Database creation sql statement
        private static final String DATABASE_CREATE = "create table if not exists "
                + TABLE_STATIONS + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_IDX + " integer, " + COLUMN_URI
                + " text not null, " + COLUMN_NAME + " text not null, " + COLUMN_ICON + " text not null);";

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
            onCreate(db);
        }

    }
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_IDX,
            MySQLiteHelper.COLUMN_URI,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_ICON,
    };

    public tkkStationsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addStation(tkkStation s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_URI, s.getUri().toString());
        values.put(MySQLiteHelper.COLUMN_NAME, s.getName());
        values.put(MySQLiteHelper.COLUMN_ICON, s.getIconURI().toString());

        long insertId = database.insert(MySQLiteHelper.TABLE_STATIONS, null, values);
        s.setIndex(((int) insertId));
    }

    public tkkStation createStation(String n, Uri u) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_URI, u.toString());
        values.put(MySQLiteHelper.COLUMN_NAME, n);
        values.put(MySQLiteHelper.COLUMN_ICON, "test");

        long insertId = database.insert(MySQLiteHelper.TABLE_STATIONS, null, values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_STATIONS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        tkkStation newStation = cursorToStation(cursor);
        cursor.close();
        return newStation;
    }


    public void deleteStation (tkkStation station) {
        long id = station.getId();
        System.out.println("tkkStation deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_STATIONS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public void updateStation(tkkStation s){
        ContentValues cv = new ContentValues();
        cv.put(MySQLiteHelper.COLUMN_IDX, s.getIndex());
        cv.put(MySQLiteHelper.COLUMN_NAME, s.getName());
        cv.put(MySQLiteHelper.COLUMN_URI, s.getUri().toString());
        cv.put(MySQLiteHelper.COLUMN_ICON, s.getIconURI().toString());

        database.update(MySQLiteHelper.TABLE_STATIONS, cv, "_id=" + s.getId(), null);

    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_STATIONS, null, null);
    }

    public ArrayList<tkkStation> getAllStations() {
        ArrayList<tkkStation> stations = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_STATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tkkStation station = cursorToStation(cursor);
            stations.add(station);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return stations;
    }

    private tkkStation cursorToStation(Cursor cursor) {
        return new tkkStation(cursor.getLong(0), cursor.getString(1), Uri.parse(cursor.getString(2)));
    }
/*
    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }
    */

}
