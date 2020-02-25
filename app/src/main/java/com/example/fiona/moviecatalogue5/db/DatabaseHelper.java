package com.example.fiona.moviecatalogue5.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmovietv";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL UNIQUE," +
                    " %s TEXT NOT NULL UNIQUE," +
                    " %s TEXT NOT NULL UNIQUE)",
            DatabaseContract.MovieColumns.TABLE_MOVIE,
            DatabaseContract.MovieColumns._ID,
            DatabaseContract.MovieColumns.NAMA,
            DatabaseContract.MovieColumns.DESKRIPSI,
            DatabaseContract.MovieColumns.FOTO
    );
    private static final String SQL_CREATE_TABLE_TV= String.format("CREATE TABLE %s"
                    +"(%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "%s TEXT NOT NULL UNIQUE,"+
                    "%s TEXT NOT NULL UNIQUE,"+
                    "%s TEXT NOT NULL UNIQUE)",
            DatabaseContract.TvShowColumns.TABLE_TV,
            DatabaseContract.TvShowColumns._ID,
            DatabaseContract.TvShowColumns.NAMA,
            DatabaseContract.TvShowColumns.DESKRIPSI,
            DatabaseContract.TvShowColumns.FOTO
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.MovieColumns.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TvShowColumns.TABLE_TV);

        onCreate(db);
    }

}
