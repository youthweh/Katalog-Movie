package com.example.fiona.moviecatalogue5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;


import java.util.ArrayList;


public class SearchMovieActivity extends AppCompatActivity {
    private MovieAdapter adapter;
    private MovieViewModel movieModel;
    private ArrayList<Favorite> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie);

        adapter = new MovieAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieModel.getMoviesSearch().observe(this, getMovie);

        adapter.setData(movies);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search_mv)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search1));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    Toast.makeText(SearchMovieActivity.this, q, Toast.LENGTH_SHORT).show();
                    movieModel.findMovie(q);
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String q) {
                    return false;
                }
            });
        }
        return true;
    }

    private Observer<ArrayList<Favorite>> getMovie = new Observer<ArrayList<Favorite>>() {
        @Override
        public void onChanged(ArrayList<Favorite> movie) {
            if (movies != null) {
                adapter.setData(movie);
            }
        }
    };

}
