package com.dam2023.firestore;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    public void saveNote(View v) {

        String titre = etTitre.getText().toString();
        String note = etNote.getText().toString();

        Log.i(TAG, "saveNote ■ " + titre + " / " + note);

        Map<String, Object> contenuNote = new HashMap<>();
        contenuNote.put(KEY_TITRE, titre);
        contenuNote.put(KEY_NOTE, note);

        // envoi des données Firestore
        noteRef3.set(contenuNote)
                // tout s'est bien passé
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Note enregistrée: ", Toast.LENGTH_SHORT).show();
                    }
                })
                // en cas d'erreur
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERREUR dans l'envoi", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNote(View v) {
        noteRef3.get()
                // tout s'est bien passé
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String titre = documentSnapshot.getString(KEY_TITRE);
                            String note = documentSnapshot.getString(KEY_NOTE);
                            tvSaveNote.setText("Titre de la note: " + titre + "\nNote: " + note);
                        } else {
                            Toast.makeText(MainActivity.this, "Le document n'existe pas !", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                // en cas d'erreur
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERREUR de lecture", Toast.LENGTH_SHORT).show();
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