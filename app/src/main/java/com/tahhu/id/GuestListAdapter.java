package com.tahhu.id;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GuestListAdapter extends BaseAdapter {
    private Context context;
    private List<String> guestList;

    public GuestListAdapter(Context context, List<String> guestList) {
        this.context = context;
        this.guestList = guestList;
    }

    @Override
    public int getCount() {
        return guestList.size();
    }

    @Override
    public Object getItem(int position) {
        return guestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(guestList.get(position));

        return convertView;
    }
}