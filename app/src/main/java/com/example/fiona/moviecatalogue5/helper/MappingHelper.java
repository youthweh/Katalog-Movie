package com.example.fiona.moviecatalogue5.helper;

import android.database.Cursor;

import com.example.fiona.moviecatalogue5.Favorite;
import com.example.fiona.moviecatalogue5.Movie;
import com.example.fiona.moviecatalogue5.db.DatabaseContract;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Favorite> mapCursorToArrayList(Cursor moviesCursor){
        ArrayList<Favorite> moviesList = new ArrayList<>();

        while (moviesCursor.moveToNext()){
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
            String nama = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.NAMA));
            String deskripsi = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESKRIPSI));
            String foto = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.FOTO));
            moviesList.add(new Favorite(id, nama, deskripsi, foto));
        }

        return moviesList;
    }
    public static Favorite mapCursorToObject(Cursor moviesCursor) {
        moviesCursor.moveToFirst();
        int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns._ID));
        String nama = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.NAMA));
        String deskripsi = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.DESKRIPSI));
        String foto = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(DatabaseContract.MovieColumns.FOTO));
        return new Favorite(id, nama, deskripsi, foto);
    }
}
