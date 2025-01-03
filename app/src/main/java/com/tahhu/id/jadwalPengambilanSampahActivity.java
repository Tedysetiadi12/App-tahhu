package com.tahhu.id;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class jadwalPengambilanSampahActivity extends AppCompatActivity {
    private EditText inputKabupaten, inputKecamatan, inputKelurahan, inputDusun;
    private Button btnLihatJadwal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_jadwal_pengambilan_sampah);
        initializeViews();
        setupListeners();
        ImageView iconNotification = findViewById(R.id.iconNotification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kabupaten = inputKabupaten.getText().toString();
                String kecamatan = inputKecamatan.getText().toString();
                String kelurahan = inputKelurahan.getText().toString();
                String dusun = inputDusun.getText().toString();

                if (kabupaten.isEmpty() || kecamatan.isEmpty() || kelurahan.isEmpty() || dusun.isEmpty()) {
                    showErrorDialog("Harap mengisi semua alamat dengan lengkap.");
                } else {
                    showNotificationPopup();
                }

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Menutup aktivitas
            }
        });

    }

    // Method untuk memainkan suara notifikasi
    private void playNotificationSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void initializeViews() {
        inputKabupaten = findViewById(R.id.inputKabupaten);
        inputKecamatan = findViewById(R.id.inputKecamatan);
        inputKelurahan = findViewById(R.id.inputKelurahan);
        inputDusun = findViewById(R.id.inputDusun);
        btnLihatJadwal = findViewById(R.id.btnLihatJadwal);
    }

    private void setupListeners() {
        btnLihatJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kabupaten = inputKabupaten.getText().toString();
                String kecamatan = inputKecamatan.getText().toString();
                String kelurahan = inputKelurahan.getText().toString();
                String dusun = inputDusun.getText().toString();

                if (kabupaten.isEmpty() || kecamatan.isEmpty() || kelurahan.isEmpty() || dusun.isEmpty()) {
                    showErrorDialog("Harap mengisi semua field alamat.");
                } else {
                    showJadwalPopup(kabupaten, kecamatan, kelurahan, dusun);
                }
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
    private String generateRandomTime() {
        int hour = 6 + (int)(Math.random() * ((10 - 6) + 1)); // Jam antara 6-10
        int minute = (int)(Math.random() * 60); // Menit antara 0-59
        return String.format("%02d:%02d", hour, minute); // Format HH:MM
    }

    private void showJadwalPopup(String kabupaten, String kecamatan, String kelurahan, String dusun) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popup_jadwal, null);
        TextView jadwalText = dialogView.findViewById(R.id.textJadwal);

        // Hasilkan waktu acak untuk jadwal
        String waktuPagi = generateRandomTime();
        String waktuSiang = generateRandomTime();

        // Buat teks jadwal berdasarkan input
        String jadwal = "Jadwal pengambilan sampah di:\n\n"
                + "Kabupaten: " + kabupaten + "\n"
                + "Kecamatan: " + kecamatan + "\n"
                + "Kelurahan: " + kelurahan + "\n"
                + "Dusun: " + dusun + "\n\n"
                + "Hari: Senin dan Kamis\n"
                + "Pukul: " + waktuPagi + " - " + waktuSiang;

        jadwalText.setText(jadwal);

        AlertDialog dialog = new AlertDialog.Builder(this).create();

// Tambahkan ikon X untuk menutup
        ImageView closeIcon = dialogView.findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

// Tampilkan dialog dengan layout kustom
        dialog.setView(dialogView);
        dialog.show();

    }

    // Notifikasi Popup
    private void showNotificationPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.popup_notification_settings, null);

        // Close Icon
        AlertDialog dialog = builder.setView(dialogView).create();
        ImageView closeIcon = dialogView.findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Switch for Notification
        Switch notificationSwitch = dialogView.findViewById(R.id.switchNotification);

        // Set default state to off
        notificationSwitch.setChecked(false);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Notifikasi diaktifkan", Toast.LENGTH_SHORT).show();
                    playNotificationSound();
                } else {
                    Toast.makeText(getApplicationContext(), "Notifikasi dinonaktifkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show Popup
        dialog.show();
    }

    // Optional: Add transition animation when opening new activities
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}