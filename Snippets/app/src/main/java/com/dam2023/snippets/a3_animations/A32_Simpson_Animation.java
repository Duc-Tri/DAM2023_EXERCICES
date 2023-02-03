package com.dam2023.snippets.a3_animations;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.dam2023.snippets.R;

public class A32_Simpson_Animation extends AppCompatActivity {

    ImageView ivTitle, ivHomer, ivBart;
    float MAX_X, MAX_Y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a32_simpson_animation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivTitle = findViewById(R.id.ivSimpsonsTitle);
        ivHomer = findViewById(R.id.ivHomerSimpson);
        ivBart = findViewById(R.id.ivBartSimpson);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        Log.d(TAG, "largeur : " + metrics.widthPixels);
//        Log.d(TAG, "hauteur : " + metrics.heightPixels);
        // metrics.densityDpi
//        px = dp * (dpi/160)   ■   dp = px/(dpi/160) ◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄◄
        final float SCREEN_HEIGHT_DP = (float) metrics.heightPixels / metrics.densityDpi * 160;
        final float SCREEN_WIDTH_DP = (float) metrics.widthPixels / metrics.densityDpi * 160;
        final float SCREEN_HEIGHT_PX = metrics.heightPixels;
        final float SCREEN_WIDTH_PX = metrics.widthPixels;

        System.out.println("SCREEN_WIDTH_DP:" + SCREEN_WIDTH_DP + " ■ SCREEN_HEIGHT_DP:" + SCREEN_HEIGHT_DP);

        //ivHomer.setY(SCREEN_HEIGHT_DP + ivHomer.getHeight());
        ivHomer.setX(SCREEN_WIDTH_PX + ivHomer.getWidth() + 500);
        ivBart.setX(SCREEN_WIDTH_PX + ivBart.getWidth());
        ivBart.setY(SCREEN_HEIGHT_PX / 2 - ivBart.getHeight() - 100);

        final int TITLE_DURATION = 500;
        final int HOMER_DURATION = 5000;
        final int BART_DURATION = 4000;
        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivTitle.animate().rotation(36000).setDuration(TITLE_DURATION);
                ivTitle.animate().alpha(0).setDuration(TITLE_DURATION);
                ivTitle.animate().scaleX(0).setDuration(TITLE_DURATION);
                ivTitle.animate().scaleY(0).setDuration(TITLE_DURATION);

                ivHomer.animate().x(-ivHomer.getWidth()).setDuration(HOMER_DURATION);
                ivBart.animate().x(-ivBart.getWidth()).setDuration(BART_DURATION);
            }
        });
    }


}