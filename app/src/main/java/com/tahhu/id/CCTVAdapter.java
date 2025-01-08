package com.tahhu.id;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CCTVAdapter extends RecyclerView.Adapter<CCTVAdapter.ViewHolder> {
    private final Context context;
    private final List<CCTVData> cctvList;

    public CCTVAdapter(Context context, List<CCTVData> cctvList) {
        this.context = context;
        this.cctvList = cctvList;
        setupSSLContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cctv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CCTVData cctv = cctvList.get(position);
        holder.name.setText(cctv.getName());
        holder.location.setText(cctv.getLocation());
        String videoUrl = cctv.getVideoUrl();
        holder.itemView.setOnClickListener(v -> showPopupVideo(videoUrl));
    }

    @Override
    public int getItemCount() {
        return cctvList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cctv_name);
            location = itemView.findViewById(R.id.cctv_location);
        }
    }

    private void setupSSLContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupVideo(String videoUrl) {
        if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://")) {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.dialog_cctv_video);
            dialog.setCancelable(true);

            PlayerView playerView = dialog.findViewById(R.id.playerView);
            ExoPlayer player = new ExoPlayer.Builder(context).build();
            playerView.setPlayer(player);

            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setAllowCrossProtocolRedirects(true)
                    .setConnectTimeoutMs(15000)
                    .setReadTimeoutMs(15000)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            MediaSource mediaSource;
            if (videoUrl.endsWith(".m3u8")) {
                mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(videoUrl));
            } else {
                mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(videoUrl));
            }

            player.setMediaSource(mediaSource);
            player.prepare();
            player.setPlayWhenReady(true);

            player.addListener(new Player.Listener() {
                @Override
                public void onPlayerError(PlaybackException error) {
                    Toast.makeText(context, "Error playing video: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(dialogInterface -> {
                player.stop();
                player.release();
            });

            dialog.show();
        } else {
            Toast.makeText(context, "Invalid video URL", Toast.LENGTH_SHORT).show();
        }
    }
}

