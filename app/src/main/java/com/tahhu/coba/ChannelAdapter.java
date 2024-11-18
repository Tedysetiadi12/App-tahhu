package com.tahhu.coba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelAdapter extends BaseAdapter {
    private Context context;
    private String[] channelNames;
    private int[] channelImages;
    private String[] channelUrls;

    public ChannelAdapter(Context context, String[] channelNames, int[] channelImages, String[] channelUrls) {
        this.context = context;
        this.channelNames = channelNames;
        this.channelImages = channelImages;
        this.channelUrls = channelUrls;
    }

    @Override
    public int getCount() {
        return channelNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false);
        }

        TextView channelName = convertView.findViewById(R.id.channel_name);
        ImageView channelImage = convertView.findViewById(R.id.channel_image);

        channelName.setText(channelNames[position]);
        channelImage.setImageResource(channelImages[position]);

        return convertView;
    }
}
