package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class SelectionTransport extends AppCompatActivity {
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_transport);

        // Ambil data "address" dan "destination" dari Intent yang dikirimkan
        String addressFromPreviousActivity = getIntent().getStringExtra("address");
        String destinationFromPreviousActivity = getIntent().getStringExtra("destination");

        // Inisialisasi Button untuk opsi transportasi
        Button optionMobil = findViewById(R.id.optionMobil);
        Button optionMotor = findViewById(R.id.optionMotor);
        Button optionPickup = findViewById(R.id.optionPickup);

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

        // Set OnClickListener untuk semua Button
        View.OnClickListener transportClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (v.getId() == R.id.optionMobil) {
                    // Arahkan ke CarSelectionActivity
                    intent = new Intent(SelectionTransport.this, CarSelectionActivity.class);
                    intent.putExtra("selection_info", "Mobil");
                } else if (v.getId() == R.id.optionMotor) {
                    // Arahkan ke BikeSelectionActivity
                    intent = new Intent(SelectionTransport.this, BikeSelectionActivity.class);
                    intent.putExtra("selection_info", "Motor");
                } else if (v.getId() == R.id.optionPickup) {
                    // Arahkan ke PickupSelectionActivity
                    intent = new Intent(SelectionTransport.this, KirimBarangActivity.class);
                    intent.putExtra("selection_info", "Kirim Barang");
                } else {
                    return; // Jika Button tidak dikenali, keluar dari metode
                }

                // Tambahkan data yang diterima ke Intent
                intent.putExtra("address", addressFromPreviousActivity);
                intent.putExtra("destination", destinationFromPreviousActivity);
                // Mulai aktivitas yang sesuai
                startActivity(intent);
            }
        };

        // Tetapkan listener untuk semua tombol
        optionMobil.setOnClickListener(transportClickListener);
        optionMotor.setOnClickListener(transportClickListener);
        optionPickup.setOnClickListener(transportClickListener);

        // Inisialisasi btn_back
        ImageView btnBack = findViewById(R.id.btn_back);
        // Menangani klik pada tombol kembali
        btnBack.setOnClickListener(v -> {
            // Menyelesaikan aktivitas saat tombol kembali diklik
            onBackPressed();
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(SelectionTransport.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(SelectionTransport.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(SelectionTransport.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
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
