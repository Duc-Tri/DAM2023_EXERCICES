package com.dam2023.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity3";
    private EditText etTitre, etNote;
    private TextView tvShowNote3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");


    public void initUi() {
        etTitre = (EditText) findViewById(R.id.etTitre);
        etNote = (EditText) findViewById(R.id.etNote);
        tvShowNote3 = (TextView) findViewById(R.id.tvShowNote3);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initUi();
    }

    public void addNote(View v) {
        String titre = etTitre.getText().toString();
        String note = etNote.getText().toString();
        Note contenuNote = new Note(titre, note);

        notebookRef.add(contenuNote)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity3.this, "Enregistrement de " + titre, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity3.this, "ERREUR de l'ajout", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNotes(View v) {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String notes = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note contenuNote = documentSnapshot.toObject(Note.class);
                            contenuNote.setDocumentId(documentSnapshot.getId());

                            String documentId = contenuNote.getDocumentId();
                            String titre = contenuNote.getTitre();
                            String note = contenuNote.getNote();

                            notes += documentId + "\nTitre: " + titre + "\nNote: " + note + "\n\n";
                        }
                        tvShowNote3.setText(notes);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                String notes = "";
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Note contenuNote = documentSnapshot.toObject(Note.class);
                    contenuNote.setDocumentId(documentSnapshot.getId());


                    String documentId = contenuNote.getDocumentId();
                    String titre = contenuNote.getTitre();
                    String note = contenuNote.getNote();

                    notes += documentId + "\nTitre: " + titre + "\nNote: " + note + "\n\n";
                }

                tvShowNote3.setText(notes);

            }
        });
    }

}