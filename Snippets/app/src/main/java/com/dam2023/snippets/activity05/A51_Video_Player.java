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

        String uriPath = ("android.resource://" + getPackageName() + "/raw/" + R.raw.mp_ts_drunk);
        Uri uri = Uri.parse(uriPath);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        videoPosition = videoView.getCurrentPosition();
        Log.i("ANDROID", "onPause - POSITION:" + videoPosition);
        //outState.putInt("VideoPosition", position /*+120*1000*/);

        videoView.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ANDROID", "onStart - POSITION:" + videoPosition);
        videoView.seekTo(videoPosition);
        videoView.start();
    }

}