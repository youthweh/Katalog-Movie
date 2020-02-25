package com.example.fiona.moviecatalogue5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fiona.moviecatalogue5.Favorite;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.fiona.moviecatalogue5.db.DatabaseContract.TvShowColumns.DESKRIPSI;
import static com.example.fiona.moviecatalogue5.db.DatabaseContract.TvShowColumns.FOTO;
import static com.example.fiona.moviecatalogue5.db.DatabaseContract.TvShowColumns.NAMA;
import static com.example.fiona.moviecatalogue5.db.DatabaseContract.TvShowColumns.TABLE_TV;

public class TvShowHelper {
    private static final String DATABASE_TABLE = TABLE_TV;
    private static DatabaseHelper dataBaseHelper;
    private Context context;
    private static TvShowHelper INSTANCE;
    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    //untuk mengambil data
    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    //untuk mengambil data dengan id tertentu
    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public Favorite getFavoriteById(int id) {
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        Favorite fav = new Favorite();
        if (cursor.getCount() > 0) {
            do {
                fav.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                fav.setNama(cursor.getString(cursor.getColumnIndexOrThrow(NAMA)));
                fav.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(DESKRIPSI)));
                fav.setFoto(cursor.getString(cursor.getColumnIndexOrThrow(FOTO)));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return fav;
    }


    public long insert(Favorite favorite){
        ContentValues args = new ContentValues();
        args.put(NAMA,favorite.getNama());
        args.put(DESKRIPSI,favorite.getDeskripsi());
        args.put(FOTO,favorite.getFoto());
        return database.insert(DATABASE_TABLE,null,args);
    }


    //untuk menghapus data
    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public Boolean cekNama(String nama){
        Cursor c = database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + NAMA+ " " + " LIKE " +"'"+nama+"'",null);
        c.moveToFirst();
        if (c.getCount() > 0 ){
            return true;
        }else if(c.getCount() == 0){
            return false;
        }
        return false;
    }

}
