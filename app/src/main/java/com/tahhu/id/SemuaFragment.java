package com.tahhu.id;

import android.os.Bundle;
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
    private List<Piutang> piutangList;


    public SemuaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semua, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSemua);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list
        piutangList = new ArrayList<>();

        // Get the user ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the Firebase Realtime Database
        DatabaseReference notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("piutang");

        // Fetch data from Firebase and filter by "Lunas" status
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                piutangList.clear();  // Clear previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Piutang piutang = snapshot.getValue(Piutang.class);
                    // Use getContext() to get the context of the fragment
                    Toast.makeText(getContext(), "Data berhasil muncul!", Toast.LENGTH_SHORT).show();
//                    Log.d("Piutang", "Data: " + piutang.getNama());
                    piutangList.add(piutang);
                }
                // Notify adapter of data change
                if (adapter == null) {
                    adapter = new PiutangAdapter(piutangList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged(); // Notify that data has changed
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
