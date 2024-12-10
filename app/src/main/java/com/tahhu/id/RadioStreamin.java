package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;


public class RadioStreamin extends AppCompatActivity {
    private boolean isPlaying = false;
    private ImageView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_streamin);

        playButton = findViewById(R.id.play_pause);
        ImageView back = findViewById(R.id.back_beranda);

        // Navigate back to MainActivity
        back.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        // Toggle play and pause with the play button
        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopService(new Intent(RadioStreamin.this, BackgroundSoundService.class));
                playButton.setImageResource(R.drawable.play_icon);
            } else {
                startService(new Intent(RadioStreamin.this, BackgroundSoundService.class));
                playButton.setImageResource(R.drawable.pause);
            }
            isPlaying = !isPlaying;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(RadioStreamin.this, BackgroundSoundService.class));
    }
}
