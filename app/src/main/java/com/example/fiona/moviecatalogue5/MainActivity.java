package com.example.fiona.moviecatalogue5;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.favorite_menu) {
            Intent mIntent = new Intent(MainActivity.this, FavActivity.class);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.search_movie) {
            Intent mIntent = new Intent(MainActivity.this, SearchMovieActivity.class);
            startActivity(mIntent);
        } else if (item.getItemId() == R.id.search_tvshow) {
            Intent mIntent = new Intent(MainActivity.this, SearchTvShowActivity.class);
            startActivity(mIntent);
        }
        else if (item.getItemId() == R.id.reminder_menu) {
            Intent mIntent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
