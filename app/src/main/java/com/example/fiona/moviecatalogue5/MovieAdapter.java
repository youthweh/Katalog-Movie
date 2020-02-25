package com.example.fiona.moviecatalogue5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Favorite> mData = new ArrayList<>();
    private Context context;
    public MovieAdapter(Context context) {
        this.context = context;
    }
    public MovieAdapter(){

    }
    public void setData(ArrayList<Favorite> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(final Favorite item) {
        mData.add(item);
        notifyDataSetChanged();
    }
    public void clearData() {
        mData.clear();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MovieViewHolder holder, int position) {
        holder.bind(mData.get(position));
        final Favorite mv = mData.get(position);
        String gambar = "https://image.tmdb.org/t/p/w185" + mv.getFoto();
        Glide.with(holder.itemView.getContext())
                .load(gambar)
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);
        holder.mvName.setText(mv.getNama());
        holder.mvDescription.setText(mv.getDeskripsi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(holder.itemView.getContext(), "Kamu memilih " + mData.get(holder.getAdapterPosition()).getNama(), Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(holder.itemView.getContext(),MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE,mData.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView mvName, mvDescription;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mv_item_photo);
            mvName = itemView.findViewById(R.id.mv_item_name);
            mvDescription = itemView.findViewById(R.id.mv_item_description);
        }

        void bind(Favorite movie){
            String gambar = "https://image.tmdb.org/t/p/w185"+movie.getFoto();
            mvName.setText(movie.getNama());
            mvDescription.setText(movie.getDeskripsi());
            Glide.with(itemView.getContext())
                    .load(gambar)
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
        }
    }
}
