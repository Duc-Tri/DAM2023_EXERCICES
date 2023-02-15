package com.dam2023.firestoreemailauthentification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // 1 *** définiton des globales

    private Button btn_logout;

    // 2 ***
    public void initUI()
    {
        btn_logout=findViewById(R.id.btn_logout);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
}