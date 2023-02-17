package com.dam2023.dam_pixabay_json;

import static com.dam2023.dam_pixabay_json.Constantes.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // composants graphiques
        ImageView ivImageView = findViewById(R.id.ivPixabayPhoto);
        TextView tvCreator = findViewById(R.id.tvPixaBayPhotoCreator);
        TextView tvLikes = findViewById(R.id.tvPixaBayPhotoLikes);

        // récupération data
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creator = intent.getStringExtra(EXTRA_CREATOR);
        int likes = intent.getIntExtra(EXTRA_LIKES,999);

        tvCreator.setText(creator);
        tvLikes.setText(" ❤ ♥ " + likes);

        // Gestion des erreurs d'affichage ou de chargement de l'image
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher_round);

//      Context c = holder.ivImageView.getContext(); // SI ERREUR QUAND ON PASSE LA VUE A GLIDE

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImageView);

    }
}