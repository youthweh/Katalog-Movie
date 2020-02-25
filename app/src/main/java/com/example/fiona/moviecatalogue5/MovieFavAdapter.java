package com.example.fiona.moviecatalogue5;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavAdapter.MovieFavViewHolder> {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    @NonNull
    @Override
    public MovieFavAdapter.MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new MovieFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieFavAdapter.MovieFavViewHolder holder, int position) {
        String gambar = "https://image.tmdb.org/t/p/w185"+ listMoviesFav.get(position).getFoto();
        Glide.with(holder.imgPhoto)
                .load(gambar)
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);
        holder.mvName.setText(listMoviesFav.get(position).getNama());
        holder.mvDescription.setText(listMoviesFav.get(position).getDeskripsi());
        holder.cvMovie.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Toast.makeText(holder.cvMovie.getContext(), "Kamu memilih " + listMoviesFav.get(position).getNama(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE,listMoviesFav.get(position));
                holder.cvMovie.getContext().startActivity(intent);
            }
        }));

    }

    @Override
    public int getItemCount() {
        return listMoviesFav.size();
    }

    public class MovieFavViewHolder extends RecyclerView.ViewHolder {
       final ImageView imgPhoto;
       final TextView mvName, mvDescription;
       final CardView cvMovie;
        public MovieFavViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mv_item_photo);
            mvName = itemView.findViewById(R.id.mv_item_name);
            mvDescription = itemView.findViewById(R.id.mv_item_description);
            cvMovie = itemView.findViewById(R.id.card_view);
        }
    }

    private ArrayList<Favorite> listMoviesFav = new ArrayList<>();
    private Activity activity;

    public MovieFavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Favorite> getListMoviesFav() {
        return listMoviesFav;
    }

    public void setListMoviesFav(ArrayList<Favorite> listMoviesFav) {
        if (listMoviesFav.size() > 0) {
            this.listMoviesFav.clear();
        }
        this.listMoviesFav.addAll(listMoviesFav);
        notifyDataSetChanged();
    }

}
