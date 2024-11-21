package com.tahhu.coba;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cctv extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CCTVAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;

    private final HashMap<String, List<CCTVData>> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup data
        setupData();

        // Setup dropdown
        setupDropdown();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        loadData("Yogyakarta"); // Default wilayah Jogja
    }

    private void setupData() {
        List<CCTVData> jogjaList = new ArrayList<>();
        jogjaList.add(new CCTVData("Simpang rejowinangun", "Yogyakarta", "https://livepantau.semarangkota.go.id/hls/414/4501/2024/8795266c-ebc3-4a95-8827-b82360403f0a_4501.m3u8"));
        jogjaList.add(new CCTVData("Simpang Gayam", "Yogyakarta", "https://cctvjss.jogjakota.go.id/atcs/ATCS_gayam.stream/playlist.m3u8"));
        jogjaList.add(new CCTVData("Nol KM", "Yogyakarta", "https://cctvjss.jogjakota.go.id/atcs/ATCS_kmnol.stream/playlist.m3u8"));
        jogjaList.add(new CCTVData("Jalan Gajah", "Yogyakarta", "https://cctvjss.jogjakota.go.id/tps-liar/S3_Jalan_Gajah.stream/playlist.m3u8"));
        jogjaList.add(new CCTVData("Pasar Beringharjo", "Yogyakarta", "https://cctvjss.jogjakota.go.id/malioboro/Malioboro_30_Pasar_Beringharjo.stream/playlist.m3u8"));
        jogjaList.add(new CCTVData("Teteg", "Yogyakarta", "https://cctvjss.jogjakota.go.id/margo-utomo/Wisma-Ratih.stream/playlist.m3u8"));
        jogjaList.add(new CCTVData("DPRD", "Yogyakarta", "https://cctvjss.jogjakota.go.id/malioboro/Malioboro_4_Depan_DPRD.stream/playlist.m3u8"));

        List<CCTVData> jakartaList = new ArrayList<>();
        jakartaList.add(new CCTVData("GBK", "Jakarta", "https://cctv.balitower.co.id/Gelora-017-700470_2/index.m3u8"));
        jakartaList.add(new CCTVData("DPR", "Jakarta", "https://cctv.balitower.co.id/Bendungan-Hilir-003-700014_1/index.m3u8"));
        jakartaList.add(new CCTVData("Tanjung Duren", "Jakarta", "https://cctv.balitower.co.id/Tomang-004-702108_2/index.m3u8"));
        jakartaList.add(new CCTVData("Jati Pulo", "Jakarta", "https://cctv.balitower.co.id/Jati-Pulo-001-702017_2/index.m3u8"));
        jakartaList.add(new CCTVData("Jati Pulo", "Jakarta", "https://cctv.balitower.co.id/Manggarai-Pintu-Air_1/index.m3u8"));

        List<CCTVData> klatenList = new ArrayList<>();
        klatenList.add(new CCTVData("Simpang 3 ngingas", "klaten", "https://stream.klaten.go.id:8080/cctv/hls/simpang3ngingas_arahsolo.m3u8"));
        klatenList.add(new CCTVData("Simpang 4 Bareng", "Klaten", "https://stream.klaten.go.id:8080/cctv/hls/simpang4bareng_ptz.m3u8"));
        klatenList.add(new CCTVData("Simpang 5 Matahari", "Klaten", "https://stream.klaten.go.id:8080/cctv/hls/simpang5matahari_ptz.m3u8"));
        klatenList.add(new CCTVData("Simpang 4 Pemda", "Klaten", "https://stream.klaten.go.id:8080/cctv/hls/simpang5matahari_ptz.m3u8"));
        klatenList.add(new CCTVData("Simpang Al-Aqsa", "Klaten", "https://stream.klaten.go.id:8080/cctv/hls/simpangalaqsa_ptz.m3u8"));

        List<CCTVData> semarangList = new ArrayList<>();
        semarangList.add(new CCTVData("Pucang Gading", "Semarang", "https://livepantau.semarangkota.go.id/hls/414/4501/2024/8795266c-ebc3-4a95-8827-b82360403f0a_4501.m3u8"));
        semarangList.add(new CCTVData("Milo", "Semarang", "https://livepantau.semarangkota.go.id/hls/414/601/2024/8795266c-ebc3-4a95-8827-b82360403f0a_601.m3u8"));
        semarangList.add(new CCTVData("Simpang Lima", "Semarang", "https://livepantau.semarangkota.go.id/hls/414/4101/2024/8795266c-ebc3-4a95-8827-b82360403f0a_4101.m3u8"));
        semarangList.add(new CCTVData("Tugu Muda", "Semarang", "https://livepantau.semarangkota.go.id/hls/414/101/2024/8795266c-ebc3-4a95-8827-b82360403f0a_101.m3u8"));
        semarangList.add(new CCTVData("Anjasmoro", "Semarang", "https://livepantau.semarangkota.go.id/hls/414/4601/2024/8795266c-ebc3-4a95-8827-b82360403f0a_4601.m3u8"));

        dataMap.put("Yogyakarta", jogjaList);
        dataMap.put("Jakarta", jakartaList);
        dataMap.put("Klaten", klatenList);
        dataMap.put("Semarang", semarangList);
    }

    private void setupDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(dataMap.keySet()));
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRegion = parent.getItemAtPosition(position).toString();
            loadData(selectedRegion);
        });
    }

    private void loadData(String region) {
        List<CCTVData> cctvList = dataMap.get(region);
        if (cctvList != null) {
            adapter = new CCTVAdapter(this, cctvList);
            recyclerView.setAdapter(adapter);
        }

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
