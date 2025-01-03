package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanfasilitasAdapter extends RecyclerView.Adapter<PeminjamanfasilitasAdapter.ViewHolder> {

    private List<PeminjamanFasilitasModel> peminjamanList;


    public PeminjamanfasilitasAdapter(List<PeminjamanFasilitasModel> peminjamanList) {
        this.peminjamanList = peminjamanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peminjaman, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeminjamanFasilitasModel peminjaman = peminjamanList.get(position);
        holder.nama.setText("Nama Peminjam : "+peminjaman.getNama());
        holder.noHp.setText("No Hanphone : "+peminjaman.getNoHp());
        holder.alamat.setText("Alamat : "+peminjaman.getAlamat());
        holder.keperluan.setText("Keperluan : "+peminjaman.getKeperluan());
        holder.fasilitas.setText("Fasilitas : "+peminjaman.getFasilitas());
    }

    @Override
    public int getItemCount() {
        return peminjamanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, noHp, alamat, keperluan, fasilitas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            noHp = itemView.findViewById(R.id.noHp);
            alamat = itemView.findViewById(R.id.alamat);
            keperluan = itemView.findViewById(R.id.keperluan);
            fasilitas = itemView.findViewById(R.id.fasilitas);
        }
    }
}

