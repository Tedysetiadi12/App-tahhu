package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class peminjamanFasilitasActivity extends AppCompatActivity {
    private EditText edtNama, edtNoHp, edtAlamat, edtKeperluan;
    private Button btnPilihFasilitas, btnTambahPeminjaman;
    private RecyclerView recyclerView;

    private List<PeminjamanFasilitasModel> peminjamanList;
    private PeminjamanfasilitasAdapter adapter;

    private String[] fasilitasOptions = {"Aula", "Taman", "Lapangan Olahraga"};
    private String selectedFasilitas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_peminjaman_fasilitas);

        // Inisialisasi Views
        edtNama = findViewById(R.id.inputNama);
        edtNoHp = findViewById(R.id.inputNoHp);
        edtAlamat = findViewById(R.id.inputAlamat);
        edtKeperluan = findViewById(R.id.inputKeperluan);
        btnPilihFasilitas = findViewById(R.id.btnPilihFasilitas);
        btnTambahPeminjaman = findViewById(R.id.btnTambahPeminjaman);
        recyclerView = findViewById(R.id.cardRecyclerView);

        // Inisialisasi List dan Adapter
        peminjamanList = new ArrayList<>();
        adapter = new PeminjamanfasilitasAdapter(peminjamanList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Tombol untuk memilih fasilitas
        btnPilihFasilitas.setOnClickListener(view -> showFasilitasDialog());

        // Tombol untuk menambah peminjaman
        btnTambahPeminjaman.setOnClickListener(view -> addPeminjaman());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas
            }
        });
    }

    private void showFasilitasDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Fasilitas");

        builder.setItems(fasilitasOptions, (dialog, which) -> {
            selectedFasilitas = fasilitasOptions[which];
            btnPilihFasilitas.setText(selectedFasilitas);
        });

        builder.create().show();
    }

    private void addPeminjaman() {
        String nama = edtNama.getText().toString();
        String noHp = edtNoHp.getText().toString();
        String alamat = edtAlamat.getText().toString();
        String keperluan = edtKeperluan.getText().toString();

        if (nama.isEmpty() || noHp.isEmpty() || alamat.isEmpty() || keperluan.isEmpty() || selectedFasilitas.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tambahkan data ke daftar
        PeminjamanFasilitasModel peminjaman = new PeminjamanFasilitasModel(nama, noHp, alamat, keperluan, selectedFasilitas);
        peminjamanList.add(peminjaman);
        adapter.notifyDataSetChanged();

        // Bersihkan input
        edtNama.setText("");
        edtNoHp.setText("");
        edtAlamat.setText("");
        edtKeperluan.setText("");
        btnPilihFasilitas.setText("Pilih Fasilitas");
        selectedFasilitas = "";

        Toast.makeText(this, "Peminjaman berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
    }
}
