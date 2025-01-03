package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class LayananKeamananActivity extends AppCompatActivity {

    private TextInputEditText etDeskripsi, etLokasi;
    private Button btnLaporkan;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_keamanan);

        initializeViews();
        setClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etDeskripsi = findViewById(R.id.etDeskripsi);
        etLokasi = findViewById(R.id.etLokasi);
        btnLaporkan = findViewById(R.id.btnLaporkan);
        tvStatus = findViewById(R.id.tvStatus);
    }

    private void setClickListeners() {
        btnLaporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laporkanKejadian();
            }
        });
    }

    private void laporkanKejadian() {
        String deskripsi = etDeskripsi.getText().toString().trim();
        String lokasi = etLokasi.getText().toString().trim();

        if (deskripsi.isEmpty() || lokasi.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement actual reporting logic here (e.g., send to server)

        // For now, we'll just show a success message
        tvStatus.setText("Laporan berhasil dikirim. Petugas keamanan akan segera menuju lokasi.");
        Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show();

        // Clear input fields
        etDeskripsi.setText("");
        etLokasi.setText("");
    }
}