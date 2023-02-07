package com.dam2023.snippets.activity05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam2023.snippets.R;
import com.google.android.material.navigation.NavigationBarView;

public class A53_Spinner extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinXMLJava, spinJavaJava;

    private void setSpinnerXMLJava() {
        spinXMLJava = findViewById(R.id.spinnerXMLJava);
        // récupère la liste depuis le XML strings
        String listStudents[] = getResources().getStringArray(R.array.students);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listStudents);

        spinXMLJava.setAdapter(adapter);

        spinXMLJava.setOnItemSelectedListener(this);
    }


    String[] itemsList = {"Toto", "Tata", "Titi"};

    private void setSpinnerJavaJava() {
        spinJavaJava = findViewById(R.id.spinnerJavaJava);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_list_item_1, itemsList);
        spinJavaJava.setAdapter(adapter);
        spinJavaJava.setOnItemSelectedListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a53_spinner);

        setSpinnerXMLJava();
        setSpinnerJavaJava();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        final Toast myToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);

        switch (parent.getId()) {
            case R.id.spinnerXMLJava:
                myToast.setText("spinnerXMLJava ■ selected: " + spinXMLJava.getSelectedItem().toString());
                myToast.show();
                break;

            case R.id.spinnerJavaJava:
                myToast.setText("spinnerJavaJava ■ selected: " + spinJavaJava.getSelectedItem().toString());
                myToast.show();
                break;

            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}