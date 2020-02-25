package com.example.fiona.moviecatalogue5;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fiona.moviecatalogue5.db.DatabaseContract;
import com.example.fiona.moviecatalogue5.db.MovieHelper;
import com.example.fiona.moviecatalogue5.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;
import android.database.ContentObserver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovFavFragment extends Fragment implements LoadMoviesCallback  {
    private ProgressBar progressBar;
    private RecyclerView rvMovie;
    private MovieFavAdapter adapter;
    private MovieHelper movieHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public MovFavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mov_fav,container,false);
        // Inflate the layout for this fragment
        progressBar = view.findViewById(R.id.progressBar);
        rvMovie = view.findViewById(R.id.rv_movies);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setHasFixedSize(true);
        adapter = new MovieFavAdapter(getActivity());
        rvMovie.setAdapter(adapter);

        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.open();

        new LoadMoviesAsync(this.getContext(), this).execute();

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this.getContext());
        getContext().getContentResolver().registerContentObserver(DatabaseContract.MovieColumns.CONTENT_URI_MOVIE, true, myObserver);

        if (savedInstanceState == null){
            new LoadMoviesAsync(this.getContext(), this).execute();
        }else{
            ArrayList<Favorite> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null){
                adapter.setListMoviesFav(list);
            }
        }

        return view;
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListMoviesFav());
    }

    @Override
    public void postExecute(ArrayList<Favorite> favorites) {
        progressBar.setVisibility(View.INVISIBLE);
        if (favorites.size() > 0){
            adapter.setListMoviesFav(favorites);
        }else{
            adapter.setListMoviesFav(new ArrayList<Favorite>());
            showSnackbarMessage("Tidak ada data film kesukaam saat ini");
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvMovie, message, Snackbar.LENGTH_SHORT).show();
    }

    private static class LoadMoviesAsync extends AsyncTask<Void, Void, ArrayList<Favorite>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;
        private LoadMoviesAsync(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Favorite> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieColumns.CONTENT_URI_MOVIE,null,null,null,null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }
        @Override
        protected void onPostExecute(ArrayList<Favorite> favorites) {
            super.onPostExecute(favorites);
            weakCallback.get().postExecute(favorites);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }
    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMoviesAsync(context, (LoadMoviesCallback) context).execute();
        }
    }

}

interface LoadMoviesCallback {
    void preExecute();
    void postExecute(ArrayList<Favorite> favorites);
}