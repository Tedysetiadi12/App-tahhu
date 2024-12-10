package com.tahhu.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<Video> videoList;
    private ExoPlayer currentPlayer = null;  // Menyimpan player yang sedang aktif

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);

        // Inisialisasi ExoPlayer
        ExoPlayer player = new ExoPlayer.Builder(context).build();
        holder.playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(video.getUrl());
        player.setMediaItem(mediaItem);
        player.prepare();

        // Mulai pemutaran jika video muncul
        if (currentPlayer != null) {
            currentPlayer.pause(); // Hentikan video sebelumnya
            currentPlayer.setVolume(0); // Mute video sebelumnya
        }

        currentPlayer = player;
        player.play(); // Mulai pemutaran video

        // Set like dan comment count
        holder.likeCount.setText(String.valueOf(video.getLikes()));
        holder.commentCount.setText(String.valueOf(video.getComments()));

        // Tombol Like, Comment, Share
        holder.likeButton.setOnClickListener(v -> {
            video.setLikes(video.getLikes() + 1);
            holder.likeCount.setText(String.valueOf(video.getLikes()));
        });

        holder.commentButton.setOnClickListener(v -> {
            // Handle comment action
        });

        holder.shareButton.setOnClickListener(v -> {
            // Handle share action
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // Fungsi untuk menghentikan semua video
    public void stopAllVideos() {
        if (currentPlayer != null) {
            currentPlayer.release(); // Hentikan dan bebaskan resource
            currentPlayer = null;
        }
    }


    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        ImageView likeButton, commentButton, shareButton;
        TextView likeCount, commentCount;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
        }
    }
}
