package com.dam2023.snippets.a3_animations;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.dam2023.snippets.R;

public class A31_Splash_Animation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a31_splash_animation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView imageView_loading = (ImageView) findViewById(R.id.loading);
        imageView_loading.animate().rotation(36000).setDuration(2000);
    }
}