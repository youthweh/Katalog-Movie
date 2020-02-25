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

public class SearchTvShowActivity extends AppCompatActivity {
    private TvShowAdapter adapter;
    private TvShowViewModel tvShowViewModel;
    private ArrayList<Favorite> tvs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie);

        adapter = new TvShowAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getTvsSearch().observe(this, getTV);

        adapter.setData(tvs);
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
            searchView.setQueryHint(getResources().getString(R.string.search2));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    Toast.makeText(SearchTvShowActivity.this, q, Toast.LENGTH_SHORT).show();
                    tvShowViewModel.findTvShow(q);
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

    private Observer<ArrayList<Favorite>> getTV = new Observer<ArrayList<Favorite>>() {
        @Override
        public void onChanged(ArrayList<Favorite> tv) {
            if (tvs != null) {
                adapter.setData(tv);
            }
        }
    };
}
