package com.tahhu.coba;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.util.Log;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class CCTVAdapter extends RecyclerView.Adapter<CCTVAdapter.ViewHolder> {
    private final Context context;
    private final List<CCTVData> cctvList;

    public CCTVAdapter(Context context, List<CCTVData> cctvList) {
        this.context = context;
        this.cctvList = cctvList;
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

    private void showPopupVideo(String videoUrl) {
        if (!videoUrl.endsWith(".m3u8")) {
            // Jika URL adalah halaman web, buka di browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            context.startActivity(browserIntent);
            return;
        }

        // Membuat popup dialog untuk memutar video
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_cctv_video);
        dialog.setCancelable(true);

        PlayerView playerView = dialog.findViewById(R.id.playerView);
        ExoPlayer player = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(player);

        // Menyiapkan video
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        // Menghentikan video saat dialog ditutup
        dialog.setOnDismissListener(dialogInterface -> player.release());

        dialog.show();
    }

}
