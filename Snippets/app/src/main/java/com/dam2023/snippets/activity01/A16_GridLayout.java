package com.dam2023.snippets.activity01;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dam2023.snippets.R;

public class A16_GridLayout extends AppCompatActivity {

    private static final String TAG = "A16_GridLayout";

    public void playSound(View view) {
        Button buttonPressed = (Button) view;
        Log.i(TAG, "playSound: "+buttonPressed.getId());

        String tag=buttonPressed.getTag().toString();
        MediaPlayer mediaPlayer=MediaPlayer.create(this,
                getResources().getIdentifier(tag,"raw",getPackageName()));
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a16_grid_layout);

    }
}