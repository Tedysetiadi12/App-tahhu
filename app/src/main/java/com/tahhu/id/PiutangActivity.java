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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PiutangActivity extends AppCompatActivity {

    private Button btnTambah;

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
            Piutang piutang = new Piutang(type, jumlah, tanggal, jatuhTempo, nama, deskripsi, catatan, status);

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
