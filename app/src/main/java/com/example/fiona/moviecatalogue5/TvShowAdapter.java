package com.example.fiona.moviecatalogue5;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private ArrayList<Favorite> mData = new ArrayList<>();
    private Context context;
    public TvShowAdapter(Context context){
        this.context = context;
    }
    public TvShowAdapter(){

    }
    public void setData(ArrayList<Favorite> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowAdapter.TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_movie, parent, false);
        return new TvShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowAdapter.TvShowViewHolder holder, int position) {
        holder.bind(mData.get(position));
        final Favorite tv = mData.get(position);
        String gambar = "https://image.tmdb.org/t/p/w185" + tv.getFoto();
        Glide.with(holder.itemView.getContext())
                .load(gambar)
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);
        holder.mvName.setText(tv.getNama());
        holder.mvDescription.setText(tv.getDeskripsi());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), "Kamu memilih" + mData.get(holder.getAdapterPosition()).getNama(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(),TvShowDetailActivity.class);
                intent.putExtra(TvShowDetailActivity.EXTRA_TV,mData.get(holder.getAdapterPosition()));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class TvShowViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView mvName, mvDescription;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.mv_item_photo);
            mvName = itemView.findViewById(R.id.mv_item_name);
            mvDescription = itemView.findViewById(R.id.mv_item_description);
        }

        void bind(Favorite tv) {
            String gambar = "https://image.tmdb.org/t/p/w185" + tv.getFoto();
            mvName.setText(tv.getNama());
            mvDescription.setText(tv.getDeskripsi());
            Glide.with(itemView.getContext())
                    .load(gambar)
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPhoto);
        }
    }
}

