package com.dam2023.projectfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeDatabaseActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private Button btnMessage;
    private EditText etMessage;

    private TextView tvReadMessage;

    private static final String TAG = "RealTimeActivity";


    private void initUI() {
        btnMessage = (Button) findViewById(R.id.btnSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        tvReadMessage = (TextView) findViewById(R.id.tvReadMessage);
    }

    public void setOnClickListener() {
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(etMessage.getText().toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_database);

        initUI();

        setOnClickListener();

        setValueEventListener();
    }

    public void setValueEventListener() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String val = snapshot.getValue(String.class);
                tvReadMessage.setText(val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value", error.toException());
            }
        });
    }

}

/*
MD5: 9B:7F:75:24:7D:5C:04:3E:07:E2:9E:E7:A6:94:7B:78
SHA1: 6C:53:EF:97:F6:E9:1E:1D:AC:01:F9:4E:29:30:C7:83:57:4D:CF:0A
SHA-256: 83:48:3E:49:4E:E9:D2:37:1D:0D:E0:27:A2:48:3A:77:2F:8D:F2:85:52:BB:F5:F3:50:73:0A:96:0D:39:10:07
Valid until: dimanche 17 septembre 2051
*/