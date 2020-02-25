package com.example.fiona.moviecatalogue5;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.fiona.moviecatalogue5.helper.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavFragment extends Fragment implements LoadTvsCallback {

    private ProgressBar progressBar;
    private RecyclerView rvMovie;
    private TvShowFavAdapter adapter;
    private TvShowHelper tvHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public TvShowFavFragment() {
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
        adapter = new TvShowFavAdapter(getActivity());
        rvMovie.setAdapter(adapter);

        tvHelper = TvShowHelper.getInstance(getContext());
        tvHelper.open();

        new TvShowFavFragment.LoadTvsAsync(tvHelper, this).execute();

        if (savedInstanceState == null){
            new TvShowFavFragment.LoadTvsAsync(tvHelper, this).execute();
        }else{
            ArrayList<Favorite> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null){
                adapter.setListTvShowsFav(list);
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
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListTvShowsFav());
    }

    @Override
    public void postExecute(ArrayList<Favorite> favorites) {
        progressBar.setVisibility(View.INVISIBLE);
        if (favorites.size() > 0){
            adapter.setListTvShowsFav(favorites);
        }else{
            adapter.setListTvShowsFav(new ArrayList<Favorite>());
            showSnackbarMessage("Tidak ada tv kesukaan saat ini");
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvMovie, message, Snackbar.LENGTH_SHORT).show();
    }

    private static class LoadTvsAsync extends AsyncTask<Void, Void, ArrayList<Favorite>> {
        private final WeakReference<TvShowHelper> weakTvHelper;
        private final WeakReference<LoadTvsCallback> weakCallback;
        private LoadTvsAsync(TvShowHelper tvShowHelper, LoadTvsCallback callback) {
            weakTvHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }
        @Override
        protected ArrayList<Favorite> doInBackground(Void... voids) {
            Cursor dataCursor = weakTvHelper.get().queryAll();
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
        tvHelper.close();
    }

}

interface LoadTvsCallback {
    void preExecute();
    void postExecute(ArrayList<Favorite> favorites);
}

