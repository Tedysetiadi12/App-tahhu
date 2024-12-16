package com.tahhu.id;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PiutangActivity extends AppCompatActivity {

    private Button btnTambah;
    private long convertToMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);


        btnTambah = findViewById(R.id.button);

        btnTambah.setOnClickListener(view -> showAddDialog());

        // Set up ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link TabLayout and ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Semua");
                    break;
                case 1:
                    tab.setText("Belum Bayar");
                    break;
                case 2:
                    tab.setText("Lunas");
                    break;
            }
        }).attach();

        calculatePiutang();
    }

    private void calculatePiutang() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("piutang");

        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPiutang = 0;
                int totalJatuhTempo = 0;
                int totalBelumBayar = 0;
                int totalLunas = 0;
                int totalPiutangLunas = 0; // Tambahkan variabel ini untuk menghitung total piutang lunas

                long todayMillis = System.currentTimeMillis();

                for (DataSnapshot piutangSnapshot : snapshot.getChildren()) {
                    Piutang piutang = piutangSnapshot.getValue(Piutang.class);

                    if (piutang != null) {
                        // Konversi jumlah menjadi integer
                        int jumlah = Integer.parseInt(piutang.getJumlah());

                        // Tambahkan ke total piutang
                        totalPiutang += jumlah;

                        // Periksa status
                        if (piutang.getStatus().equalsIgnoreCase("Belum Lunas")) {
                            totalBelumBayar += jumlah;

                            // Periksa apakah jatuh tempo
                            long jatuhTempoMillis = convertToMillis(piutang.getJatuhTempo());
                            if (jatuhTempoMillis < todayMillis) {
                                totalJatuhTempo += jumlah;
                            }
                        } else if (piutang.getStatus().equalsIgnoreCase("Lunas")) {
                            // Tambahkan ke total lunas
                            totalLunas += jumlah;

                            // Hitung total piutang lunas
                            totalPiutangLunas += jumlah;  // Akumulasikan jumlah piutang yang lunas
                        }
                    }
                }

                // Perbarui UI
                updateUI(totalPiutang, totalJatuhTempo, totalBelumBayar, totalLunas, totalPiutangLunas); // Panggil dengan total piutang lunas
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PiutangActivity.this, "Gagal membaca data!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(int totalPiutang, int totalJatuhTempo, int totalBelumBayar, int totalLunas, int totalpiutanglunas) {
        TextView totalPiutangText = findViewById(R.id.totalpiutang);
        TextView nominalJatuhTempoText = findViewById(R.id.nominalJatuhTempo);
        TextView nominalBelumBayarText = findViewById(R.id.nominalBelumLunas);
        TextView nominalLunasText = findViewById(R.id.nominalLunas);
        TextView nominalTotalPiutangLunas = findViewById(R.id.nominalTotalPiutangLunas);


        totalPiutangText.setText("Rp. "+ String.valueOf(totalPiutang));
        nominalJatuhTempoText.setText("Rp. "+String.valueOf(totalJatuhTempo));
        nominalBelumBayarText.setText("Rp. "+String.valueOf(totalBelumBayar));
        nominalLunasText.setText("Rp. "+String.valueOf(totalLunas));
        nominalTotalPiutangLunas.setText("Rp. "+String.valueOf(totalpiutanglunas));
    }


    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_piutang, null);
        builder.setView(dialogView);

        // Referensi ke elemen dalam dialog
        Spinner typeSpinner = dialogView.findViewById(R.id.spinnerType);
        EditText edtJumlah = dialogView.findViewById(R.id.edtJumlah);
        TextView txtTanggal = dialogView.findViewById(R.id.txtTanggal);
        TextView txtTanggalJatuhTempo = dialogView.findViewById(R.id.txtTanggalJatuhTempo);
        EditText edtNama = dialogView.findViewById(R.id.edtNama);
        EditText edtDeskripsi = dialogView.findViewById(R.id.edtDeskripsi);
        EditText edtCatatan = dialogView.findViewById(R.id.edtCatatan);
        Spinner statusSpinner = dialogView.findViewById(R.id.spinnerStatus);
        Button btnSimpan = dialogView.findViewById(R.id.btnSimpan);
        Button btnBatal = dialogView.findViewById(R.id.btnBatal);

        // Setup pilihan untuk spinner
        String[] types = {"Piutang", "Utang"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        String[] statuses = {"Belum Lunas", "Lunas"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Set tanggal picker
        txtTanggal.setOnClickListener(v -> showDatePicker(txtTanggal));
        txtTanggalJatuhTempo.setOnClickListener(v -> showDatePicker(txtTanggalJatuhTempo));

        AlertDialog alertDialog = builder.create();

        btnSimpan.setOnClickListener(v -> {
            String type = typeSpinner.getSelectedItem().toString();
            String jumlah = edtJumlah.getText().toString().trim();
            String tanggal = txtTanggal.getText().toString();
            String jatuhTempo = txtTanggalJatuhTempo.getText().toString();
            String nama = edtNama.getText().toString().trim();
            String deskripsi = edtDeskripsi.getText().toString().trim();
            String catatan = edtCatatan.getText().toString().trim();
            String status = statusSpinner.getSelectedItem().toString();

            if (jumlah.isEmpty() || tanggal.isEmpty() || jatuhTempo.isEmpty() || nama.isEmpty() || deskripsi.isEmpty() || catatan.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get user ID from Firebase Authentication
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Reference to the Firebase Realtime Database
            DatabaseReference notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("piutang");

            // Create a unique ID for each entry
            String piutangId = notesReference.push().getKey();

            // Create a Piutang object
            Piutang piutang = new Piutang(piutangId,type, jumlah, tanggal, jatuhTempo, nama, deskripsi, catatan, status);

            // Save data to Firebase
            if (piutangId != null) {
                notesReference.child(piutangId).setValue(piutang)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                        });
            }

            alertDialog.dismiss();
        });

        btnBatal.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void showDatePicker(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    textView.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }



}
