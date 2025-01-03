package com.tahhu.id;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PembayaranIuranActivity extends AppCompatActivity {

    private RecyclerView rvIuran;
    private List<Iuran> iuranList;
    private IuranAdapter iuranAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_iuran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvIuran = findViewById(R.id.rvIuran);
        iuranList = getStaticIuranData();
        iuranAdapter = new IuranAdapter(iuranList, this::showPaymentDialog);
        rvIuran.setLayoutManager(new LinearLayoutManager(this));
        rvIuran.setAdapter(iuranAdapter);
    }

    private List<Iuran> getStaticIuranData() {
        List<Iuran> data = new ArrayList<>();
        data.add(new Iuran("1", "Iuran Bulanan", "Januari 2025", 100000, false));
        data.add(new Iuran("2", "Denda Keterlambatan", "Desember 2024", 50000, false));
        data.add(new Iuran("3", "Iuran Bulanan", "Desember 2024", 100000, true));
        return data;
    }

    private void showPaymentDialog(Iuran iuran) {
        if (iuran.isPaid()) {
            Toast.makeText(this, "Pembayaran sudah dilakukan", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_payment_iuran);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView tvDetailNama = dialog.findViewById(R.id.tvDetailNama);
        TextView tvDetailPeriode = dialog.findViewById(R.id.tvDetailPeriode);
        TextView tvDetailJumlah = dialog.findViewById(R.id.tvDetailJumlah);
        RadioGroup rgPaymentMethod = dialog.findViewById(R.id.rgPaymentMethod);
        Button btnConfirmPayment = dialog.findViewById(R.id.btnConfirmPayment);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        tvDetailNama.setText(iuran.getNama());
        tvDetailPeriode.setText(iuran.getPeriode());
        tvDetailJumlah.setText(String.format("Rp %,d", iuran.getJumlah()));

        btnConfirmPayment.setOnClickListener(v -> {
            int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }
            processPayment(iuran);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void processPayment(Iuran iuran) {
        iuran.setPaid(true);
        iuranAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Pembayaran berhasil!", Toast.LENGTH_SHORT).show();
    }

    public static class Iuran {
        private String id;
        private String nama;
        private String periode;
        private int jumlah;
        private boolean isPaid;

        public Iuran(String id, String nama, String periode, int jumlah, boolean isPaid) {
            this.id = id;
            this.nama = nama;
            this.periode = periode;
            this.jumlah = jumlah;
            this.isPaid = isPaid;
        }

        // Getters and setters remain the same
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getPeriode() {
            return periode;
        }

        public void setPeriode(String periode) {
            this.periode = periode;
        }

        public int getJumlah() {
            return jumlah;
        }

        public void setJumlah(int jumlah) {
            this.jumlah = jumlah;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public void setPaid(boolean isPaid) {
            this.isPaid = isPaid;
        }
    }

    public static class IuranAdapter extends RecyclerView.Adapter<IuranAdapter.IuranViewHolder> {
        private List<Iuran> iuranList;
        private OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(Iuran iuran);
        }

        public IuranAdapter(List<Iuran> iuranList, OnItemClickListener listener) {
            this.iuranList = iuranList;
            this.listener = listener;
        }

        @Override
        public IuranViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_iuran, parent, false);
            return new IuranViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IuranViewHolder holder, int position) {
            Iuran iuran = iuranList.get(position);
            holder.tvNama.setText(iuran.getNama());
            holder.tvPeriode.setText(iuran.getPeriode());
            holder.tvJumlah.setText(String.format("Rp %,d", iuran.getJumlah()));
            holder.tvStatus.setText(iuran.isPaid() ? "Sudah Dibayar" : "Belum Dibayar");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(
                    iuran.isPaid() ? android.R.color.holo_green_dark : android.R.color.holo_red_dark
            ));
            holder.btnBayar.setVisibility(iuran.isPaid() ? View.GONE : View.VISIBLE);
            holder.btnBayar.setOnClickListener(v -> listener.onItemClick(iuran));
        }

        @Override
        public int getItemCount() {
            return iuranList.size();
        }

        class IuranViewHolder extends RecyclerView.ViewHolder {
            TextView tvNama, tvPeriode, tvJumlah, tvStatus;
            Button btnBayar;

            IuranViewHolder(View itemView) {
                super(itemView);
                tvNama = itemView.findViewById(R.id.tvNama);
                tvPeriode = itemView.findViewById(R.id.tvPeriode);
                tvJumlah = itemView.findViewById(R.id.tvJumlah);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                btnBayar = itemView.findViewById(R.id.btnBayar);
            }
        }
    }
}