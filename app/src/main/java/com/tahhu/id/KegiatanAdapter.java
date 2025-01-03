package com.tahhu.id;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.KegiatanViewHolder> {
    private List<Kegiatan> kegiatanList;
    private OnKegiatanClickListener listener;
    private Map<String, Boolean> registrationStatus;

    public KegiatanAdapter(List<Kegiatan> kegiatanList, OnKegiatanClickListener listener,
                           Map<String, Boolean> registrationStatus) {
        this.kegiatanList = kegiatanList;
        this.listener = listener;
        this.registrationStatus = registrationStatus;
    }

    @NonNull
    @Override
    public KegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kegiatan, parent, false);
        return new KegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position) {
        Kegiatan kegiatan = kegiatanList.get(position);
        holder.tvNama.setText(kegiatan.getNama());
        holder.tvTanggal.setText(kegiatan.getTanggal());
        holder.tvTempat.setText(kegiatan.getLokasi());

        boolean isRegistered = registrationStatus.containsKey(kegiatan.getId());

        // Update status text and button
        if (isRegistered) {
            holder.tvStatus.setText("Anda sudah terdaftar");
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.successGreen));
            holder.btnAction.setText("Batalkan");
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.active_button_color));
        } else {
            holder.tvStatus.setText("Anda belum terdaftar");
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.gray));
            holder.btnAction.setText("Daftar");
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.buttonColor));
        }

        holder.btnAction.setOnClickListener(v -> {
            if (listener != null) {
                listener.onKegiatanClick(kegiatan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    static class KegiatanViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal, tvTempat, tvStatus;
        Button btnAction;

        KegiatanViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvTempat = itemView.findViewById(R.id.tvLokasi);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAction = itemView.findViewById(R.id.btnDaftar);
        }
    }

    public interface OnKegiatanClickListener {
        void onKegiatanClick(Kegiatan kegiatan);
    }
}

