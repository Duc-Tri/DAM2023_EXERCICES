package com.dam2023.dam_pixabay_json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.MyViewHolder> {

    private Context myContext;
    private ArrayList<ModelItem> itemArrayLists;

    public AdapterRecycler(Context context, ArrayList<ModelItem> itemArrayLists) {
        this.myContext = context;
        this.itemArrayLists = itemArrayLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.item_pixabay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelItem currentItem = itemArrayLists.get(position);

        String imageUrl = currentItem.getImageUrl();
        String creator = currentItem.getCreator();
        int likes = currentItem.getLikes();

        holder.tvCreator.setText(creator);
        holder.tvLikes.setText(" ❤ ♥ " + likes);

        // Gestion des erreurs d'affichage ou de chargement de l'image
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher_round);

//      Context c = holder.ivImageView.getContext(); // SI ERREUR QUAND ON PASSE LA VUE A GLIDE

        Glide.with(holder.ivImageView)
                .load(imageUrl)
                .apply(options)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivImageView);

    }

    @Override
    public int getItemCount() {
        return itemArrayLists.size();
    }


    // INNER CLASS ================================================================================
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImageView;
        private TextView tvCreator, tvLikes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageView = itemView.findViewById(R.id.ivPixabayPhoto);
            tvCreator = itemView.findViewById(R.id.tvPixaBayPhotoCreator);
            tvLikes = itemView.findViewById(R.id.tvPixaBayPhotoLikes);
        }
    }


}
