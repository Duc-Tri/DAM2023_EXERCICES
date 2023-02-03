package com.dam2023.snippets.activity05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dam2023.snippets.R;

public class A51_Video_Player extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    int videoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a51_video_player);

        videoView = findViewById(R.id.videoView);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // link vers la vidéo de DRUNK
        String uriPath = ("android.resource://" + getPackageName() + "/raw/" + R.raw.mp_ts_drunk);
        Uri uri = Uri.parse(uriPath);

        if (this.mediaController == null)
            mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        //videoView.start();
        videoPosition = 0;
        seekToPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void seekToPosition() {
        videoView.seekTo(videoPosition);
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPosition = videoView.getCurrentPosition();

        Log.i("ANDROID", "onPause - POSITION:" + videoPosition);

        videoView.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ANDROID", "onStart - POSITION:" + videoPosition);

        seekToPosition();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("VideoPosition", videoPosition);

        Log.i("ANDROID", "onSaveInstanceState - POSITION:" + videoPosition);

        videoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        videoPosition = savedInstanceState.getInt("VideoPosition");

        Log.i("ANDROID", "onRestoreInstanceState - POSITION:" + videoPosition);

        seekToPosition();
    }
}