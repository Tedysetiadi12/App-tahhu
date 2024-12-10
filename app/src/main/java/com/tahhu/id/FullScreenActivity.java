package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class FullScreenActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        PlayerView playerView = findViewById(R.id.fullscreen_player_view);
        ImageView closeButton = findViewById(R.id.close_button);
        // Get the URL from the Intent
        Intent intent = getIntent();
        String channelUrl = intent.getStringExtra("CHANNEL_URL");
        Log.d("Media_URL", channelUrl);
        // Initialize ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // Prepare and play the video
        MediaItem mediaItem = MediaItem.fromUri(channelUrl);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();


        closeButton.setOnClickListener(v -> {
            if (exoPlayer != null) {
                exoPlayer.stop();
                exoPlayer.release();
            }
            finish();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
