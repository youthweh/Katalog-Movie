package com.example.fiona.moviecatalogue5;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = "ac43d02969415550317c8131600d2c8d";
    private MutableLiveData<ArrayList<Favorite>> listMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Favorite>> listMoviesSearch = new MutableLiveData<>();

    void setMovie(final String movies) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Favorite> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" +API_KEY+ "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject mv = list.getJSONObject(i);
                        Favorite movieItems = new Favorite();
                        movieItems.setNama(mv.getString("title"));
                        movieItems.setDeskripsi(mv.getString("overview"));
                        movieItems.setFoto(mv.getString("poster_path"));
                        listItems.add(movieItems);
                    }
                    listMovies.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
        // request API
    }

    void findMovie(final String judul) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Favorite> listItems2 = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" +API_KEY+ "&language=en-US&query=" + judul;

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject mv = list.getJSONObject(i);
                        Favorite movieItems = new Favorite();
                        movieItems.setNama(mv.getString("title"));
                        movieItems.setDeskripsi(mv.getString("overview"));
                        movieItems.setFoto(mv.getString("poster_path"));
                        listItems2.add(movieItems);
                    }
                    listMoviesSearch.postValue(listItems2);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
        // request API
    }
    LiveData<ArrayList<Favorite>> getMovies() {
        return listMovies;
    }
    LiveData<ArrayList<Favorite>> getMoviesSearch() {
        return listMoviesSearch;
    }
}
