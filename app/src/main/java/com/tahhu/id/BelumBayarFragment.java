package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class BelumBayarFragment extends Fragment {

    private RecyclerView recyclerView;
    private PiutangAdapter adapter;

    public BelumBayarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_belum_bayar, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBelumBayar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi adapter
        adapter = new PiutangAdapter();
        recyclerView.setAdapter(adapter);

        // Dapatkan user ID dari Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Referensi ke Firebase Realtime Database
        DatabaseReference notesReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("piutang");

        // Ambil data dari Firebase
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Piutang> updatedList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Piutang piutang = snapshot.getValue(Piutang.class);
                    if (piutang != null && "Belum Lunas".equalsIgnoreCase(piutang.getStatus())) {
                        updatedList.add(piutang);
                    }
                }

                // Perbarui adapter
                adapter.submitList(updatedList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
