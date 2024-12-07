package com.tahhu.coba;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> dates;

    public CalendarAdapter(Context context, List<String> dates) {
        this.context = context;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 120));
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        textView.setText(dates.get(position));
        textView.setBackgroundColor(position % 2 == 0 ? 0xFFE3F2FD : 0xFFFFFFFF); // Alternating colors
        textView.setTextSize(16);
        return textView;
    }
}
