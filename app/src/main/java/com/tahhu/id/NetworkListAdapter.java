package com.tahhu.id;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NetworkListAdapter extends BaseAdapter {
    private Context context;
    private List<ScanResult> scanResults;

    public NetworkListAdapter(Context context, List<ScanResult> scanResults) {
        this.context = context;
        this.scanResults = scanResults;
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.network_list_item, parent, false);
        }

        TextView ssidTextView = convertView.findViewById(R.id.ssid_text_view);
        TextView signalStrengthTextView = convertView.findViewById(R.id.signal_strength_text_view);

        ScanResult scanResult = scanResults.get(position);
        ssidTextView.setText(scanResult.SSID);
        signalStrengthTextView.setText(String.format("%d dBm", scanResult.level));

        return convertView;
    }
}