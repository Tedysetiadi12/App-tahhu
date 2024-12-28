package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ActiveBookingsFragment extends Fragment implements BookingAdapter.BookingActionListener {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> activeBookings;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_bookings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activeBookings = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), activeBookings, true, this);
        recyclerView.setAdapter(bookingAdapter);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("security");
        loadActiveBookings();

        return view;
    }

    private void loadActiveBookings() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activeBookings.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Booking booking = snapshot.getValue(Booking.class);
                    if (booking != null && booking.isActive()) {
                        activeBookings.add(booking);
                    }
                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onCompleteBooking(Booking booking) {
        booking.setActive(false);
        databaseReference.child(booking.getId()).setValue(booking);
    }

    @Override
    public void onTrackBooking(Booking booking) {
        Intent intent = new Intent(getContext(), TrackingActivity.class);
        intent.putExtra("bookingId", booking.getId());
        startActivity(intent);
    }
}

