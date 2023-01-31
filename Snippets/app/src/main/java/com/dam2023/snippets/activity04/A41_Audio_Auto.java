package com.dam2023.snippets.activity04;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.dam2023.snippets.R;

import java.util.Timer;
import java.util.TimerTask;

public class A41_Audio_Auto extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a41_audio_auto);

        mediaPlayer = MediaPlayer.create(this, R.raw.mp_audio_uptown_funk);

        // VOLUME ==========================
        SeekBar sbVolume = findViewById(R.id.sbVolume);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //volume max
        int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        sbVolume.setMax(volumeMax);

        int volumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sbVolume.setProgress(volumeCurrent);

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // POSITION SEEKBAR ====================
        SeekBar sbPosition = findViewById(R.id.sbPosition);
        sbPosition.setMax(mediaPlayer.getDuration());

        sbPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myPause(sbPosition);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myPlay(sbPosition);
                mediaPlayer.seekTo(sbPosition.getProgress());
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sbPosition.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 300);

    }

    public void myPlay(View view) {
        mediaPlayer.start();
    }

    public void myPause(View view) {
        mediaPlayer.pause();
    }

}
