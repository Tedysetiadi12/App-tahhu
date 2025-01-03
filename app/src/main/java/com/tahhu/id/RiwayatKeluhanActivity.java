package com.tahhu.id;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RiwayatKeluhanActivity extends AppCompatActivity {

    private RecyclerView rvKeluhanRiwayat;
    private List<LayananKeluhanActivity.Keluhan> keluhanList;
    private LayananKeluhanActivity.KeluhanAdapter keluhanAdapter;
    private DatabaseReference keluhanReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_keluhan);

        // Inisialisasi komponen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvKeluhanRiwayat = findViewById(R.id.rvKeluhanRiwayat);
        rvKeluhanRiwayat.setLayoutManager(new LinearLayoutManager(this));

        keluhanList = new ArrayList<>();
        keluhanAdapter = new LayananKeluhanActivity.KeluhanAdapter(keluhanList);
        rvKeluhanRiwayat.setAdapter(keluhanAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        keluhanReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("keluhan");

        loadKeluhanData();
    }

    private void loadKeluhanData() {
        keluhanReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keluhanList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LayananKeluhanActivity.Keluhan keluhan = snapshot.getValue(LayananKeluhanActivity.Keluhan.class);
                    if (keluhan != null) {
                        keluhanList.add(keluhan);
                    }
                }
                keluhanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RiwayatKeluhanActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

