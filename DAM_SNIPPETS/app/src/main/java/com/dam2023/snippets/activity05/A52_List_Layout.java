package com.dam2023.snippets.activity05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dam2023.snippets.R;

public class A52_List_Layout extends AppCompatActivity {

    private final String[] listStudents = new String[]
            {"Michel", "Imothepu", "Mathias", "Philippe", "Nicolas", "Pierre", "Yacouba", "Jason"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a52_list_layout);
        showListView();
    }

    private void showListView() {
        ListView listView = findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                listStudents);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String valItem = (String) listView.getItemAtPosition(position);
                Toast.makeText(A52_List_Layout.this, "Le stagiaire: " + valItem, Toast.LENGTH_SHORT).show();
            }
        });
    }

}