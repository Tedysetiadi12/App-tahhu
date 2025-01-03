package com.tahhu.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import android.Manifest;

public class PendaftaranKegiatanActivity extends AppCompatActivity implements KegiatanAdapter.OnKegiatanClickListener {

    private RecyclerView rvKegiatan;
    private List<Kegiatan> kegiatanList;
    private KegiatanAdapter kegiatanAdapter;
    private DatabaseReference kegiatanReference;
    private String userId;
    private Map<String, Boolean> registrationStatus = new HashMap<>();

    private static final String CHANNEL_ID = "KegiatanChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_kegiatan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        kegiatanReference = FirebaseDatabase.getInstance().getReference("kegiatan");

        rvKegiatan = findViewById(R.id.rvKegiatan);

        kegiatanList = new ArrayList<>();
        kegiatanAdapter = new KegiatanAdapter(kegiatanList, this, registrationStatus);
        rvKegiatan.setLayoutManager(new LinearLayoutManager(this));
        rvKegiatan.setAdapter(kegiatanAdapter);

        loadDemoData();
        checkExistingRegistrations();
        createNotificationChannel();
    }

    private void checkExistingRegistrations() {
        DatabaseReference userKegiatanRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("kegiatan");

        userKegiatanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                registrationStatus.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    registrationStatus.put(snapshot.getKey(), true);
                }
                kegiatanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PendaftaranKegiatanActivity.this,
                        "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDemoData() {
        kegiatanList.clear();
        kegiatanList.add(new Kegiatan("K001", "Workshop Android Development", "2025-01-15", "Ruang Seminar A"));
        kegiatanList.add(new Kegiatan("K002", "Seminar Artificial Intelligence", "2025-01-20", "Auditorium Utama"));
        kegiatanList.add(new Kegiatan("K003", "Pelatihan Public Speaking", "2025-01-25", "Ruang Meeting B"));
        kegiatanList.add(new Kegiatan("K004", "Webinar Digital Marketing", "2025-02-01", "Online Zoom Meeting"));
        kegiatanList.add(new Kegiatan("K005", "Workshop UI/UX Design", "2025-02-05", "Lab Komputer C"));
        kegiatanAdapter.notifyDataSetChanged();

        for (Kegiatan kegiatan : kegiatanList) {
            kegiatanReference.child(kegiatan.getId()).setValue(kegiatan);
        }
    }

    private void showCancellationDialog(Kegiatan kegiatan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alasan Pembatalan");

        // Inflate custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_cancellation, null);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupReasons);
        builder.setView(view);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedButton = view.findViewById(selectedId);
                    String reason = selectedButton.getText().toString();
                    cancelRegistration(kegiatan, reason);
                } else {
                    Toast.makeText(PendaftaranKegiatanActivity.this,
                            "Silakan pilih alasan pembatalan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    private void cancelRegistration(Kegiatan kegiatan, String reason) {
        DatabaseReference userKegiatanRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("kegiatan");

        // Remove registration
        userKegiatanRef.child(kegiatan.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    registrationStatus.remove(kegiatan.getId());
                    kegiatanAdapter.notifyDataSetChanged();

                    // Save cancellation reason
                    DatabaseReference cancellationsRef = FirebaseDatabase.getInstance()
                            .getReference("cancellations")
                            .child(userId)
                            .child(kegiatan.getId());

                    Map<String, Object> cancellationData = new HashMap<>();
                    cancellationData.put("reason", reason);
                    cancellationData.put("timestamp", ServerValue.TIMESTAMP);

                    cancellationsRef.setValue(cancellationData);

                    Toast.makeText(this, "Pendaftaran dibatalkan", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal membatalkan pendaftaran", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onKegiatanClick(Kegiatan kegiatan) {
        if (registrationStatus.containsKey(kegiatan.getId())) {
            showCancellationDialog(kegiatan);
            return;
        }

        DatabaseReference userKegiatanRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("kegiatan");
        userKegiatanRef.child(kegiatan.getId()).setValue(kegiatan);

        registrationStatus.put(kegiatan.getId(), true);
        kegiatanAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Anda telah terdaftar untuk " + kegiatan.getNama(),
                Toast.LENGTH_SHORT).show();
        sendNotification(kegiatan);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kegiatan Channel";
            String description = "Channel for Kegiatan notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(Kegiatan kegiatan) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Pendaftaran Kegiatan Berhasil")
                .setContentText("Anda telah terdaftar untuk " + kegiatan.getNama())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (SecurityException e) {
            Toast.makeText(this, "Notification permission required", Toast.LENGTH_SHORT).show();
        }
    }

}