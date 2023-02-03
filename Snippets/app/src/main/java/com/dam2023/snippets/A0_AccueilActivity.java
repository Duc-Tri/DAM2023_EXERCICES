package com.dam2023.snippets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dam2023.snippets.a3_animations.A32_Simpson_Animation;
import com.dam2023.snippets.activity01.A11_LinearLayoutXml;
import com.dam2023.snippets.activity01.A12_FrameLayoutXml;
import com.dam2023.snippets.activity04.A41_Audio_Auto;
import com.dam2023.snippets.activity05.A51_Video_Player;

public class A0_AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_activity_accueil);


        TextView tvLinearLayout = findViewById(R.id.tvLinearLayout);
        tvLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A11_LinearLayoutXml.class);
                startActivity(intent);
            }
        });


        TextView tvFrameLayout = findViewById(R.id.tvFrameLayout);
        tvFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A12_FrameLayoutXml.class);
                startActivity(intent);
            }
        });

        // AUDIO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        TextView tvAudio = findViewById(R.id.tvAudio);
        tvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A41_Audio_Auto.class);
                startActivity(intent);
            }
        });

        // ANIMATION ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        TextView tvSimpsons = findViewById(R.id.tvSimpsons);
        tvSimpsons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A32_Simpson_Animation.class);
                startActivity(intent);
            }
        });

        // VIDEO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        TextView tvVideo = findViewById(R.id.tvVideo);
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A51_Video_Player.class);
                startActivity(intent);
            }
        });

    }

}