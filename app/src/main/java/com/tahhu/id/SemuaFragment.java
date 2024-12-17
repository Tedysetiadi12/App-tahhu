package com.tahhu.id;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SemuaFragment extends Fragment {

    private RecyclerView recyclerView;
    private PiutangAdapter adapter;

    public SemuaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semua, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSemua);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter and set it to RecyclerView
        adapter = new PiutangAdapter();
        recyclerView.setAdapter(adapter);

        // Check if user is authenticated
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

        // Fetch data from Firebase
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Piutang> piutangList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Piutang piutang = snapshot.getValue(Piutang.class);
                    if (piutang != null) {
                        piutangList.add(piutang);
                        Log.d("SemuaFragment", "Data: " + piutang.getNama());
                    }
                }

                // Submit data to adapter
                adapter.submitList(piutangList);
                Toast.makeText(getContext(), "Data berhasil dimuat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Gagal mengambil data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SemuaFragment", "Database error: ", databaseError.toException());
            }
        });

        return view;
    }
}
