package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class LunasFragment extends Fragment {

    private RecyclerView recyclerView;
    private PiutangAdapter adapter;

    public LunasFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lunas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewLunas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter
        adapter = new PiutangAdapter();
        recyclerView.setAdapter(adapter);

        // Get user ID from Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Pengguna belum login", Toast.LENGTH_SHORT).show();
            return view;
        }
        String userId = auth.getCurrentUser().getUid();

        // Reference to Firebase Realtime Database
        DatabaseReference notesReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("piutang");

        // Fetch data from Firebase and filter by "Lunas" status
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Piutang> piutangList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Piutang piutang = snapshot.getValue(Piutang.class);
                    if (piutang != null && "Lunas".equals(piutang.getStatus())) {
                        piutangList.add(piutang);
                    }
                }
                // Submit filtered list to the adapter
                adapter.submitList(piutangList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Gagal mengambil data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
