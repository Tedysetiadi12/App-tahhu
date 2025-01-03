package com.tahhu.id;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

// Pengumuman Fragment
public class PengumumanFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private List<Announcement> announcements;
    private AnnouncementAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengumuman, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);

        setupRecyclerView();
        setupSwipeRefresh();
        loadAnnouncements();

        return view;
    }

    private void setupRecyclerView() {
        announcements = new ArrayList<>();
        adapter = new AnnouncementAdapter(requireContext(), announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> loadAnnouncements());
    }

    private void loadAnnouncements() {
        // Simulasi loading data
        new Handler().postDelayed(() -> {
            announcements.clear();
            announcements.add(new Announcement(
                    "Pemadaman Listrik Terencana",
                    "Akan dilakukan pemadaman listrik pada hari Minggu...",
                    "Infrastruktur",
                    "10 Januari 2025"
            ));
            announcements.add(new Announcement(
                    "Jadwal Pembayaran Iuran Bulanan",
                    "Pembayaran iuran bulanan dapat dilakukan mulai tanggal...",
                    "Keuangan",
                    "15 Januari 2025"
            ));

            adapter.notifyDataSetChanged();
            swipeRefresh.setRefreshing(false);
        }, 1000);
    }
}
