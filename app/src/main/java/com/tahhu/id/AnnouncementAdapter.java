package com.tahhu.id;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

// Adapter untuk Pengumuman
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private Context context;
    private List<Announcement> announcements;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.titleText.setText(announcement.title);
        holder.contentText.setText(announcement.content);
        holder.dateText.setText(announcement.timestamp);
        holder.categoryChip.setText(announcement.category);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnnouncementDetailActivity.class);
            intent.putExtra("title", announcement.title);
            intent.putExtra("content", announcement.content);
            intent.putExtra("category", announcement.category);
            intent.putExtra("timestamp", announcement.timestamp);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, contentText, dateText;
        Chip categoryChip;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.announcementTitle);
            contentText = itemView.findViewById(R.id.announcementContent);
            dateText = itemView.findViewById(R.id.announcementDate);
            categoryChip = itemView.findViewById(R.id.categoryChip);
        }
    }
}
