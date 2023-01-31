package com.dam2023.snippets.activity01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dam2023.snippets.A0_AccueilActivity;
import com.dam2023.snippets.R;

public class A11_1_LinearLayoutVerticalXml extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a11_1_linear_vertical);
    }

    public void gestionClick(View view)
    {
        Intent intent= new Intent(A11_1_LinearLayoutVerticalXml.this, A0_AccueilActivity.class);
        startActivity(intent);
    }

}