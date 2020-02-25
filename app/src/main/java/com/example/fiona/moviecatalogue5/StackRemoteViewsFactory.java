package com.example.fiona.moviecatalogue5;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fiona.moviecatalogue5.db.DatabaseContract;
import com.example.fiona.moviecatalogue5.helper.MappingHelper;

import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    Cursor cursor;
    private ArrayList<Favorite> favorites;

    StackRemoteViewsFactory(Context context) {
        mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = mContext.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI_MOVIE, null, null, null, null);
        favorites = MappingHelper.mapCursorToArrayList(cursor);
        Binder.restoreCallingIdentity(identityToken);
        for(int i = 0 ;i < favorites.size();i++){
            Favorite listMoviesFav = favorites.get(i);
            try {
                String gambar = "https://image.tmdb.org/t/p/w185"+ listMoviesFav.getFoto();

                Bitmap bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(gambar)
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                mWidgetItems.add(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(FavMovieWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
