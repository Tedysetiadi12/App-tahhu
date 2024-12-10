package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private List<SlideItem> slideItems;
    private OnItemClickListener listener;

    // Constructor dengan OnItemClickListener
    public SlideAdapter(List<SlideItem> slideItems, OnItemClickListener listener) {
        this.slideItems = slideItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        SlideItem slideItem = slideItems.get(position);
        holder.imageView.setImageResource(slideItem.getImageResId());
        holder.textView.setText(slideItem.getTitle());

        // Set klik listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, slideItem.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public SlideViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public static class SlideItem {
        private int imageResId;
        private String title;
        private String url; // Tambahkan URL

        public SlideItem(int imageResId, String title, String url) {
            this.imageResId = imageResId;
            this.title = title;
            this.url = url;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

    // Interface untuk item click
    public interface OnItemClickListener {
        void onItemClick(int position, String url);
    }
}
