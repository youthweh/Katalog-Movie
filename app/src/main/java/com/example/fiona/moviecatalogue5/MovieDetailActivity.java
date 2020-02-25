package com.example.fiona.moviecatalogue5;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fiona.moviecatalogue5.db.MovieHelper;
import com.example.fiona.moviecatalogue5.helper.MappingHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.fiona.moviecatalogue5.db.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    private ProgressBar progressBar;

    ImageView mv_photo;
    TextView mv_name;
    TextView mv_detail;

    private Button btnSubmit;
    private Button btnDelete;
    private boolean isFavorite = false;
    private int position;
    private MovieHelper movieHelper;
    private Uri uriWithId;
    private Favorite favorite;

    public static final String EXTRA_NOTE1 = "extra_note1";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mv_photo = findViewById(R.id.mv_img);
        mv_name = findViewById(R.id.text_name);
        mv_detail = findViewById(R.id.text_description);
        btnSubmit = findViewById(R.id.btn_favorite);
        btnDelete = findViewById(R.id.btn_delete);
        progressBar = findViewById(R.id.progressMovie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Handler handler = new Handler();
        progressBar = findViewById(R.id.progressMovie);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final Favorite mv = getIntent().getParcelableExtra(EXTRA_MOVIE);
                        String url_image = "https://image.tmdb.org/t/p/w185" + mv.getFoto();
                        mv_name.setText(mv.getNama());
                        mv_detail.setText(mv.getDeskripsi());
                        Glide.with(MovieDetailActivity.this)
                                .load(url_image)
                                .apply(new RequestOptions().override(169, 250))
                                .into(mv_photo);
                        showLoading(false);

                        movieHelper = MovieHelper.getInstance(getApplicationContext());
                        movieHelper.open();

                        final Favorite fav = movieHelper.getFavoriteById(mv.getId());
                        if (fav.getId()!=0){
                            btnSubmit.setVisibility(View.GONE);
                            btnDelete.setVisibility(View.VISIBLE);

                        }else {
                            btnSubmit.setVisibility(View.VISIBLE);
                            btnDelete.setVisibility(View.GONE);
                        }
                        if (movieHelper.cekNama(mv.getNama())){
                            btnSubmit.setVisibility(View.GONE);
                        } else if(!movieHelper.cekNama(mv.getNama())){
                            btnSubmit.setVisibility(View.VISIBLE);
                        }

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                long result = movieHelper.insert(mv);
                                if(result > 0){
                                    btnSubmit.setVisibility(View.GONE);
                                    Toast.makeText(MovieDetailActivity.this, "Film kesukaan berhasil ditambah",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(MovieDetailActivity.this,"Gagal ditambah", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//tulis kode lagi disini
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDeleteAlertDialog(mv.getId());
                            }
                        });
//buat kode disini
                    }
                });
            }
        }).start();

    }

    private void showDeleteAlertDialog(final int id){
        String pesan = "Apakah kamu ingin menghapus film favorit kesukaan ini?";
        String dialog = "Terhapus dari Favorit";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dialog);
        builder.setMessage(pesan)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        movieHelper.deleteById(String.valueOf(id));
                        Toast.makeText(MovieDetailActivity.this, "Berhasil menghapus data", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MovieDetailActivity.this, FavActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("Tidak",new  DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showLoading(Boolean state){
        if (state){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
