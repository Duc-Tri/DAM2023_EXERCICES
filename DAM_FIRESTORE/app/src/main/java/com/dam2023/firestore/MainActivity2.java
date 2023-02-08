package com.dam2023.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private static final String KEY_TITRE = "titre";
    private static final String KEY_NOTE = "note";
    private EditText etTitre, etNote;
    private TextView tvSaveNote, tvShowNote;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 1ere méthode
//    private DocumentReference noteRef1 = db.collection("listeNotes").document("Ma première note");

    // 2e méthode
//    private CollectionReference collectionReference = db.collection("listeNotes");
//    private DocumentReference noteRef2 = collectionReference.document("Ma première note");

    // 3e méthode
    private DocumentReference noteRef3 = db.document("listeNotes/Ma première note");

    private void initUI() {
        etTitre = (EditText) findViewById(R.id.etTitre);
        etNote = (EditText) findViewById(R.id.etNote);
        tvSaveNote = (TextView) findViewById(R.id.tvSaveNote);
        tvShowNote = (TextView) findViewById(R.id.tvShowNote);

        randomTexts();
    }

    private void randomTexts() {
        etTitre.setText("TITRE-" + (int) (Math.random() * 1000000f));
        etNote.setText("NOTE-" + (int) (Math.random() * 1000000f));

    }

    @Override
    protected void onStart() {
        super.onStart();

        String titre = etTitre.getText().toString();
        String note = etNote.getText().toString();

        Log.i(TAG, "saveNote ■ " + titre + " / " + note);

        // ce listener tourne en tache de fond
        noteRef3.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity2.this, "ERREUR au chargement", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return; // on arrête tout
                }

                if (documentSnapshot.exists()) {
                    Note contenuNote = documentSnapshot.toObject(Note.class);
                    tvShowNote.setText("Titre: " + contenuNote.getTitre() + "\nNote: " + contenuNote.getNote());

                } else {
                    tvShowNote.setText("");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initUI();
    }


    public void saveNote(View v) {

        String titre = etTitre.getText().toString();
        String note = etNote.getText().toString();
        Note contenuNote = new Note(titre, note);

        // envoi des données Firestore
        noteRef3.set(contenuNote)
                // tout s'est bien passé
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity2.this, "Note enregistrée: ", Toast.LENGTH_SHORT).show();
                    }
                })
                // en cas d'erreur
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, "ERREUR dans l'envoi", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        //randomTexts();
    }

    public void loadNote(View v) {

        noteRef3.get()
                // tout s'est bien passé
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Note contenuNote = documentSnapshot.toObject(Note.class);
                            tvSaveNote.setText("Titre: " + contenuNote.getTitre() + "\nNote: " + contenuNote.getNote());
                        } else {
                            Toast.makeText(MainActivity2.this, "Le document n'existe pas !", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                // en cas d'erreur
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, "ERREUR de lecture", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        //randomTexts();
    }

    public void updateNote(View v) {
        String textNote = etNote.getText().toString();
        noteRef3.update(KEY_NOTE, textNote);
    }

    public void deleteNote(View v) {
        noteRef3.update(KEY_NOTE, FieldValue.delete())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity2.this, "La note est supprimée", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, "ERREUR de suppression", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void deleteAll(View v) {
        noteRef3.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity2.this, "TOUTE La note est supprimée", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, "ERREUR de DeleteAll", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}

/*
MD5: 54:39:68:67:54:3D:EC:85:BE:5B:78:AD:9D:DD:10:3B
SHA1: 3B:B1:16:34:45:AA:F9:E8:B7:71:F6:F5:EB:1F:24:32:24:51:7D:16
SHA-256: 67:E5:33:2C:32:12:48:B7:CE:2A:F3:9F:C1:EC:0A:4E:5B:1C:09:C2:D0:BC:C1:99:AB:8C:42:E8:54:06:71:72

 */