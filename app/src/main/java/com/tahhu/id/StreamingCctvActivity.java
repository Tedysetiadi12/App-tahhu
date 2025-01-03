package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamingCctvActivity extends AppCompatActivity {

    private Spinner spinnerCCTV;
    private WebView webViewCCTV;
    private Map<String, String> cctvStreams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_cctv);

        initializeViews();
        setupCCTVStreams();
        setupSpinner();
        setupWebView();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerCCTV = findViewById(R.id.spinnerCCTV);
        webViewCCTV = findViewById(R.id.webViewCCTV);
    }

    private void setupCCTVStreams() {
        cctvStreams = new HashMap<>();
        cctvStreams.put("Gerbang Utama", "https://example.com/cctv1");
        cctvStreams.put("Taman Tengah", "https://example.com/cctv2");
        cctvStreams.put("Area Parkir", "https://example.com/cctv3");
        // Add more CCTV streams as needed
    }

    private void setupSpinner() {
        List<String> cctvLocations = new ArrayList<>(cctvStreams.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cctvLocations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCCTV.setAdapter(adapter);

        spinnerCCTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = (String) parent.getItemAtPosition(position);
                loadCCTVStream(selectedLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupWebView() {
        webViewCCTV.getSettings().setJavaScriptEnabled(true);
        webViewCCTV.setWebViewClient(new WebViewClient());
    }

    private void loadCCTVStream(String location) {
        String streamUrl = cctvStreams.get(location);
        if (streamUrl != null) {
            webViewCCTV.loadUrl(streamUrl);
        }
    }
}