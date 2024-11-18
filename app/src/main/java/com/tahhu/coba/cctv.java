package com.tahhu.coba;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class cctv extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(this::loadData, 1000);
    }

    private void loadData() {
        List<CCTVData> cctvList = new ArrayList<>();
        cctvList.add(new CCTVData("Malioboro Titik Nol", "Yogyakarta", "https://atcs.tasikmalayakota.go.id/camera/leuwidahuarahbojongjengkol.m3u8"));
        cctvList.add(new CCTVData("Bundaran HI", "Jakarta", "https://atcs.tasikmalayakota.go.id/camera/leuwidahuarahbojongjengkol.m3u8"));
        cctvList.add(new CCTVData("Monas", "Jakarta", "https://atcs.tasikmalayakota.go.id/camera/leuwidahuarahbojongjengkol.m3u8"));

        CCTVAdapter adapter = new CCTVAdapter(this, cctvList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
