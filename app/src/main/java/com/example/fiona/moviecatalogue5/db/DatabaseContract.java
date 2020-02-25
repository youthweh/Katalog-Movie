package com.example.fiona.moviecatalogue5.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.fiona.moviecatalogue5";
    private static final String SCHEME = "content";
    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_MOVIE = "movie";

        //Note title
        public static final String NAMA = "nama";
        //Note description
        public static final String DESKRIPSI = "deskripsi";
        //Note date
        public static final String FOTO = "foto";
        public static final Uri CONTENT_URI_MOVIE= new Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TvShowColumns implements BaseColumns {
        public static final String TABLE_TV = "tvshow";

        //Note title
        public static final String NAMA = "nama";
        //Note description
        public static final String DESKRIPSI = "deskripsi";
        //Note date
        public static final String FOTO = "foto";
    }

}
