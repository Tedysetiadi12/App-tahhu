package com.tahhu.coba;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class CCTVVideoDialog extends Dialog {
    private final String videoUrl;
    private ExoPlayer player;

    public CCTVVideoDialog(Context context, String videoUrl) {
        super(context);
        this.videoUrl = videoUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cctv_video);

        PlayerView playerView = findViewById(R.id.playerView);

        // Inisialisasi ExoPlayer
        player = new ExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);

        // Tambahkan URL ke ExoPlayer
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);

        // Mulai pemutaran
        player.prepare();
        player.play();
    }

    @Override
    public void dismiss() {
        if (player != null) {
            player.release();
        }
        super.dismiss();
    }
}

