package com.dam2023.snippets.activity05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dam2023.snippets.R;

public class A54_RecycleView extends AppCompatActivity {

    RecyclerView rvRecyclerView;
    String[] stagiaires, descs;

    int[] avatars = {
            R.drawable.stagiaire_01,
            R.drawable.stagiaire_02,
            R.drawable.stagiaire_03,
            R.drawable.stagiaire_04,
            R.drawable.stagiaire_05,
            R.drawable.stagiaire_06,
            R.drawable.stagiaire_07,
            R.drawable.stagiaire_08,
            R.drawable.stagiaire_09,
            R.drawable.stagiaire_010,
            R.drawable.stagiaire_011,
            R.drawable.stagiaire_012,
            R.drawable.stagiaire_013,
            R.drawable.stagiaire_014};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a54_recycle_view);

        stagiaires = getResources().getStringArray(R.array.stagiaires);
        descs = getResources().getStringArray(R.array.descs);

        initRecyclerView();
    }

    private void initRecyclerView() {
        // lien avec design + initialisation
        RecyclerView rvRecycleView = findViewById(R.id.rvRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvRecycleView.setLayoutManager(layoutManager);

        // création de l'objet Adapter
        A54_AdapterRecyclerView adapter = new A54_AdapterRecyclerView(this, stagiaires, descs, avatars);

        // association avec le recyler
        rvRecycleView.setAdapter(adapter);
    }


}