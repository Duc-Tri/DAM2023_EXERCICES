package com.dam2023.snippets.activity01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dam2023.snippets.R;

public class A11_LinearLayoutXml extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a11_linear_layout);
    }

    public void gestionClick(View view)
    {
        Intent intent= new Intent(A11_LinearLayoutXml.this,A11_1_LinearLayoutVerticalXml.class);
        startActivity(intent);
    }
}