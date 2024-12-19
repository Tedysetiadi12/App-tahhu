package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class cctv extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CCTVAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;

    private final HashMap<String, List<CCTVData>> dataMap = new HashMap<>();
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ImageView imageback = findViewById(R.id.imageViewBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup data
        setupData();

        // Setup dropdown
        setupDropdown();

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        loadData("Yogyakarta"); // Default wilayah Jogja

        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        homeButton = findViewById(R.id.homeButton);
        menuMarket = findViewById(R.id.menumarket);
        shortVideo = findViewById(R.id.shortvidio);
        calculator = findViewById(R.id.Kalkulator);
        market = findViewById(R.id.Market);

        // Set click listeners
        homeButton.setOnClickListener(v -> navigateTo("home"));
        menuMarket.setOnClickListener(v -> updateActiveIcon("market"));
        shortVideo.setOnClickListener(v -> navigateTo("videos"));
        calculator.setOnClickListener(v -> navigateTo("calculator"));
        market.setOnClickListener(v -> navigateTo("cart"));
    }

    private void setupData() {
        List<CCTVData> jogjaList = new ArrayList<>();
        jogjaList.add(new CCTVData("Simpang rejowinangun", "Yogyakarta", "https://cctvjss.jogjakota.go.id/atcs/ATCS_Simpang_Rejowinangun_View_Timur.stream/playlist.m3u8"));
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

        List<CCTVData> madiunList = new ArrayList<>();
        madiunList.add(new CCTVData("Madiun Pusat", "Madiun", "http://103.149.120.205/cctv/"));

        dataMap.put("Yogyakarta", jogjaList);
        dataMap.put("Jakarta", jakartaList);
        dataMap.put("Klaten", klatenList);
        dataMap.put("Semarang", semarangList);
        dataMap.put("Madiun", madiunList);
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

    private void navigateTo(String destination) {
        Intent intent;
        switch (destination) {
            case "home":
                intent = new Intent(this, MainActivity.class);
                break;
            case "videos":
                intent = new Intent(this, ShortVidio.class);
                break;
            case "calculator":
                // Show calculator dialog
                DiscountCalculatorDialog dialog = new DiscountCalculatorDialog();
                dialog.show(getSupportFragmentManager(), "DiscountCalculatorDialog");
                return;
            case "cart":
                intent = new Intent(this, CartProductActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
        if (!destination.equals("market")) {
            finish(); // Close this activity if navigating away
        }
    }
    private void updateActiveIcon(String activeIcon) {
        int activeColor = getResources().getColor(R.color.white);
        int inactiveColor = getResources().getColor(R.color.inactive_icon);

        homeButton.setColorFilter(activeIcon.equals("home") ? activeColor : inactiveColor);
        menuMarket.setColorFilter(activeIcon.equals("market") ? activeColor : inactiveColor);
        shortVideo.setColorFilter(activeIcon.equals("videos") ? activeColor : inactiveColor);
        calculator.setColorFilter(activeIcon.equals("calculator") ? activeColor : inactiveColor);
        // Note: We don't change the color of the FloatingActionButton (market) as it's always highlighted
    }

    public static class DiscountCalculatorDialog extends DialogFragment {
        private EditText etPrice, etDiscount;
        private TextView tvInitialPrice, tvDiscount, tvFinalPrice;
        private Button btnReset;
        private TextWatcher textWatcher;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_diskon_kalkulator, container, false);

            // Initialize views
            etPrice = view.findViewById(R.id.etPrice);
            etDiscount = view.findViewById(R.id.etDiscount);
            tvInitialPrice = view.findViewById(R.id.tvInitialPrice);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvFinalPrice = view.findViewById(R.id.tvFinalPrice);
            btnReset = view.findViewById(R.id.btnReset);

            // Set up text change listener
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateDiscount();
                }
            };

            // Add text change listeners
            etPrice.addTextChangedListener(textWatcher);
            etDiscount.addTextChangedListener(textWatcher);

            // Set up reset button
            btnReset.setOnClickListener(v -> resetCalculator());

            ImageView closeButton = view.findViewById(R.id.ic_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return view;
        }

        private void calculateDiscount() {
            try {
                double price = Double.parseDouble(etPrice.getText().toString().isEmpty() ? "0" : etPrice.getText().toString());
                double discount = Double.parseDouble(etDiscount.getText().toString().isEmpty() ? "0" : etDiscount.getText().toString());

                double discountAmount = (price * discount) / 100;
                double finalPrice = price - discountAmount;

                // Format currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Update TextViews
                tvInitialPrice.setText(formatter.format(price).replace("IDR", "Rp"));
                tvDiscount.setText("-" + formatter.format(discountAmount).replace("IDR", "Rp"));
                tvFinalPrice.setText(formatter.format(finalPrice).replace("IDR", "Rp"));
            } catch (NumberFormatException e) {
                // Handle invalid input
                tvInitialPrice.setText("Rp 0");
                tvDiscount.setText("-Rp 0");
                tvFinalPrice.setText("Rp 0");
            }
        }

        private void resetCalculator() {
            etPrice.setText("");
            etDiscount.setText("");
            tvInitialPrice.setText("Rp 0");
            tvDiscount.setText("-Rp 0");
            tvFinalPrice.setText("Rp 0");
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
        }
    }
}
