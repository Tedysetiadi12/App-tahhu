package com.tahhu.coba;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LokasiAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<LokasiPenukaran> lokasiList;

    // Constructor
    public LokasiAdapter(Activity activity, ArrayList<LokasiPenukaran> lokasiList) {
        this.activity = activity;
        this.lokasiList = lokasiList;
    }

    @Override
    public int getCount() {
        return lokasiList.size();
    }

    @Override
    public Object getItem(int position) {
        return lokasiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Mendapatkan item lokasi
        LokasiPenukaran lokasi = lokasiList.get(position);

        // Menggunakan LayoutInflater untuk menginflate item layout
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_lokasi, parent, false);
        }

        // Mendapatkan referensi TextView dari item_lokasi.xml
        TextView namaLokasiTextView = convertView.findViewById(R.id.namalokasi);
        TextView alamatLokasiTextView = convertView.findViewById(R.id.alamatlokasi);

        // Mengatur nilai TextView dengan data lokasi
        namaLokasiTextView.setText(lokasi.getNamaLokasi());
        alamatLokasiTextView.setText(lokasi.getAlamat());

        return convertView;
    }
}
