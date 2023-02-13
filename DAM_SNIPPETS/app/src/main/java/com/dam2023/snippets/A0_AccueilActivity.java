package com.dam2023.snippets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dam2023.snippets.a3_animations.A32_Simpson_Animation;
import com.dam2023.snippets.activity01.A11_LinearLayoutXml;
import com.dam2023.snippets.activity01.A12_FrameLayoutXml;
import com.dam2023.snippets.activity01.A16_GridLayout;
import com.dam2023.snippets.activity04.A41_Audio_Auto;
import com.dam2023.snippets.activity05.A51_Video_Player;
import com.dam2023.snippets.activity05.A52_List_Layout;
import com.dam2023.snippets.activity05.A53_Spinner;
import com.dam2023.snippets.activity05.A54_AdapterRecyclerView;

public class A0_AccueilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_activity_accueil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setListenerForFrameLayout();
        setListenerForLinearLayout();

        setListenerForAnimation();
        setListenerForAudio();
        setListenerForVideo();

        setListenerForGridLayout();

        setListenerForListLayout();

        setListenerForSpinner();

        setListenerForRecyclerView();
    }

    private void setListenerForRecyclerView() {
        TextView tvRecycler = findViewById(R.id.tvRecycler);
        tvRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A54_AdapterRecyclerView.class);
                startActivity(intent);
            }
        });
    }

    private void setListenerForSpinner() {
        TextView tvSpinner = findViewById(R.id.tvSpinner);
        tvSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A53_Spinner.class);
                startActivity(intent);
            }
        });
    }

    private void setListenerForListLayout() {
        TextView tvListLayout = findViewById(R.id.tvListLayout);
        tvListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A52_List_Layout.class);
                startActivity(intent);
            }
        });

    }

    private void setListenerForGridLayout() {
        TextView tvGridLayout = findViewById(R.id.tvGridLayout);
        tvGridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A16_GridLayout.class);
                startActivity(intent);
            }
        });
    }

    // LINEAR LAYOUT ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private void setListenerForLinearLayout() {
        TextView tvLinearLayout = findViewById(R.id.tvLinearLayout);
        tvLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A11_LinearLayoutXml.class);
                startActivity(intent);
            }
        });
    }

    // FRAME LAYOUT ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private void setListenerForFrameLayout() {
        TextView tvFrameLayout = findViewById(R.id.tvFrameLayout);
        tvFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A12_FrameLayoutXml.class);
                startActivity(intent);
            }
        });
    }

    // ANIMATION ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private void setListenerForAnimation() {
        TextView tvSimpsons = findViewById(R.id.tvAnimation);
        tvSimpsons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A32_Simpson_Animation.class);
                startActivity(intent);
            }
        });
    }

    // AUDIO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private void setListenerForAudio() {
        TextView tvAudio = findViewById(R.id.tvAudio);
        tvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(A0_AccueilActivity.this, A41_Audio_Auto.class);
                startActivity(intent);
            }
        });
    }

    // VIDEO ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private void setListenerForVideo() {
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