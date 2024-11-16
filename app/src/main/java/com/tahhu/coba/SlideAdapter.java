package com.tahhu.coba;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<SlideItem> slideItems;

    public SlideAdapter(List<SlideItem> slideItems) {
        this.slideItems = slideItems;
    }

    @Override
    public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SlideViewHolder holder, int position) {
        SlideItem slideItem = slideItems.get(position);
        holder.imageView.setImageResource(slideItem.getImageResId());
        holder.textView.setText(slideItem.getTitle());
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

        public SlideItem(int imageResId, String title) {
            this.imageResId = imageResId;
            this.title = title;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getTitle() {
            return title;
        }
    }

}

