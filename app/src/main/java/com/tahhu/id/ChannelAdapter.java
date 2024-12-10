package com.tahhu.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ChannelAdapter extends BaseAdapter {
    private final Context context;
    private final String[] names;
    private final int[] images;
    private final String[] urls;

    public ChannelAdapter(Context context, String[] names, int[] images, String[] urls) {
        this.context = context;
        this.names = names;
        this.images = images;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.channel_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.channel_name);
        ImageView image = convertView.findViewById(R.id.channel_image);

        name.setText(names[position]);
        image.setImageResource(images[position]);

        return convertView;
    }
}
