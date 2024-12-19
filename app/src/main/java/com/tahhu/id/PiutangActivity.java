package com.tahhu.id;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PiutangActivity extends AppCompatActivity {
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;
    private Button btnTambah;
    private long convertToMillis(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

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

        btnTambah = findViewById(R.id.button);

        btnTambah.setOnClickListener(view -> showAddDialog());

        // Set up ViewPager Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Link TabLayout and ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Semua");
                    break;
                case 1:
                    tab.setText("Belum Bayar");
                    break;
                case 2:
                    tab.setText("Lunas");
                    break;
            }
        }).attach();

        calculatePiutang();
    }

    private void calculatePiutang() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("piutang");

        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalPiutang = 0;
                double totalJatuhTempo = 0;
                double totalBelumBayar = 0;
                double totalLunas = 0;
                double totalPiutangLunas = 0; // Tambahkan variabel ini untuk menghitung total piutang lunas

                long todayMillis = System.currentTimeMillis();

                for (DataSnapshot piutangSnapshot : snapshot.getChildren()) {
                    Piutang piutang = piutangSnapshot.getValue(Piutang.class);

                    if (piutang != null) {
                        // Konversi jumlah menjadi double
                        double jumlah = Double.parseDouble(piutang.getJumlah());

                        // Tambahkan ke total piutang
                        totalPiutang += jumlah;

                        // Periksa status
                        if (piutang.getStatus().equalsIgnoreCase("Belum Lunas")) {
                            totalBelumBayar += jumlah;

                            // Periksa apakah jatuh tempo
                            long jatuhTempoMillis = convertToMillis(piutang.getJatuhTempo());
                            if (jatuhTempoMillis < todayMillis) {
                                totalJatuhTempo += jumlah;
                            }
                        } else if (piutang.getStatus().equalsIgnoreCase("Lunas")) {
                            // Tambahkan ke total lunas
                            totalLunas += jumlah;

                            // Hitung total piutang lunas
                            totalPiutangLunas += jumlah;  // Akumulasikan jumlah piutang yang lunas
                        }
                    }
                }

                // Perbarui UI
                updateUI((int) totalPiutang, (int) totalJatuhTempo, (int) totalBelumBayar, (int) totalLunas, (int) totalPiutangLunas); // Panggil dengan total piutang lunas
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PiutangActivity.this, "Gagal membaca data!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateUI(int totalPiutang, int totalJatuhTempo, int totalBelumBayar, int totalLunas, int totalpiutanglunas) {
        TextView totalPiutangText = findViewById(R.id.totalpiutang);
        TextView nominalJatuhTempoText = findViewById(R.id.nominalJatuhTempo);
        TextView nominalBelumBayarText = findViewById(R.id.nominalBelumLunas);
        TextView nominalLunasText = findViewById(R.id.nominalLunas);
        TextView nominalTotalPiutangLunas = findViewById(R.id.nominalTotalPiutangLunas);


        totalPiutangText.setText("Rp. "+ String.valueOf(totalPiutang));
        nominalJatuhTempoText.setText("Rp. "+String.valueOf(totalJatuhTempo));
        nominalBelumBayarText.setText("Rp. "+String.valueOf(totalBelumBayar));
        nominalLunasText.setText("Rp. "+String.valueOf(totalLunas));
        nominalTotalPiutangLunas.setText("Rp. "+String.valueOf(totalpiutanglunas));
    }


    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_piutang, null);
        builder.setView(dialogView);

        // Referensi ke elemen dalam dialog
        // Replace Spinner with Button
        Button btnSelectType = dialogView.findViewById(R.id.btnSelectType);
        Button btnSelectStatus = dialogView.findViewById(R.id.btnSelectStatus);
        EditText edtJumlah = dialogView.findViewById(R.id.edtJumlah);
        TextView txtTanggal = dialogView.findViewById(R.id.txtTanggal);
        TextView txtTanggalJatuhTempo = dialogView.findViewById(R.id.txtTanggalJatuhTempo);
        EditText edtNama = dialogView.findViewById(R.id.edtNama);
        EditText edtDeskripsi = dialogView.findViewById(R.id.edtDeskripsi);
        EditText edtCatatan = dialogView.findViewById(R.id.edtCatatan);
        Button btnSimpan = dialogView.findViewById(R.id.btnSimpan);
        Button btnBatal = dialogView.findViewById(R.id.btnBatal);

        // Handle button click for selecting type
        btnSelectType.setOnClickListener(v -> showTypeSelectionDialog(btnSelectType));

        // Handle button click for selecting status
        btnSelectStatus.setOnClickListener(v -> showStatusSelectionDialog(btnSelectStatus));

        // Set tanggal picker
        txtTanggal.setOnClickListener(v -> showDatePicker(txtTanggal));
        txtTanggalJatuhTempo.setOnClickListener(v -> showDatePicker(txtTanggalJatuhTempo));

        AlertDialog alertDialog = builder.create();

        btnSimpan.setOnClickListener(v -> {
            String type = btnSelectType.getText().toString();
            String jumlah = edtJumlah.getText().toString().trim();
            String tanggal = txtTanggal.getText().toString();
            String jatuhTempo = txtTanggalJatuhTempo.getText().toString();
            String nama = edtNama.getText().toString().trim();
            String deskripsi = edtDeskripsi.getText().toString().trim();
            String catatan = edtCatatan.getText().toString().trim();
            String status = btnSelectStatus.getText().toString();

            if (jumlah.isEmpty() || tanggal.isEmpty() || jatuhTempo.isEmpty() || nama.isEmpty() || deskripsi.isEmpty() || catatan.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get user ID from Firebase Authentication
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Reference to the Firebase Realtime Database
            DatabaseReference notesReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("piutang");

            // Create a unique ID for each entry
            String piutangId = notesReference.push().getKey();

            // Create a Piutang object
            Piutang piutang = new Piutang(piutangId,type, jumlah, tanggal, jatuhTempo, nama, deskripsi, catatan, status);

            // Save data to Firebase
            if (piutangId != null) {
                notesReference.child(piutangId).setValue(piutang)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                        });
            }

            alertDialog.dismiss();
        });

        btnBatal.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }
    private void showTypeSelectionDialog(Button button) {
        // Show a dialog to select type (Piutang / Utang)
        String[] types = {"Piutang", "Utang"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Tipe");
        builder.setItems(types, (dialog, which) -> button.setText(types[which]));
        builder.show();
    }

    private void showStatusSelectionDialog(Button button) {
        // Show a dialog to select status (Belum Lunas / Lunas)
        String[] statuses = {"Belum Lunas", "Lunas"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Status");
        builder.setItems(statuses, (dialog, which) -> button.setText(statuses[which]));
        builder.show();
    }
    private void showDatePicker(TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    textView.setText(date);
                }, year, month, day);
        datePickerDialog.show();
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
