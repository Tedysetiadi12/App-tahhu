package com.tahhu.id;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {
    private EditText etItemName, etQuantity, etPrice, etNotes;
    private Button btnSelectCategory, btnAddToList, btnViewCalendar, btnBoilEgg, btnSteamRice, btnSteamVeg, btnBoilNoodle, btnSetCustom;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalAmount, timerText;
    private String selectedCategory = "";
    private ActiveListFragment activeListFragment;
    private CompletedListFragment completedListFragment;
    public DatabaseReference shoppingListRef;
    private String userId;
    private FloatingActionButton btnStart, btnReset;
    private Spinner notificationSpinner;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 0;
    private NumberPicker hoursPicker, minutesPicker, secondsPicker;
    public double totalCompletedAmount = 0; // Total pengeluaran selesai
    private TabLayout tabLayout2;
    private EditText etInput, etResult;
    private Spinner spinnerFrom, spinnerTo;
    private Button btnLihatReferensi;
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

    // Conversion factors for volume (in mL)
    private final Map<String, Double> volumeFactors = new HashMap<String, Double>() {{
        put("Cangkir (Tepung, Beras)", 250.0);
        put("Sdm (Sendok Makan)", 15.0);
        put("sdt (Sendok Teh) (Bumbu, Rempah)", 5.0);
        put("ml (Militer)", 1.0);
        put("L (Liter)", 1000.00);
        put("gelas (Air, Susu)", 215.0);

        put("kg (Kilogram)", 1000.0);
        put("gr (Gram)", 1.0);
        put("ons (Daging, Sayur)", 28.3495);
        put("pound (lb) (Resep Barat)", 453.592);
    }};

    // Conversion factors for weight (in gram)
    private final Map<String, Double> weightFactors = new HashMap<String, Double>() {{
        put("gr (Gram)", 1.0);
        put("kg (Kilogram)", 1000.0);
        put("ons (Daging, Sayur)", 28.3495);
        put("pound (lb) (Resep Barat)", 453.592);
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Firebase initialization
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        shoppingListRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("shopping_list");

        initializeViews();
        setupViewPager();
        setupListeners();
        setupSpinner();
        setupClickListeners();
        setupNumberPickers();
        setupTabLayout2();
        setupSpinners2();
        setupListeners2();

        // ActiveItems dan CompletedItems akan berada di dalam "shopping_list"
        activeListFragment = new ActiveListFragment(shoppingListRef.child("activeItems"));
        completedListFragment = new CompletedListFragment(shoppingListRef.child("completedItems"));

        completedListFragment.getCompletedItemsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("CompletedListFragment", "onDataChange dipanggil. Jumlah item: " + snapshot.getChildrenCount());
                totalCompletedAmount = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    ShoppingItem item = data.getValue(ShoppingItem.class);
                    if (item != null) {
                        totalCompletedAmount += (item.getPrice() * item.getQuantity());
                    }
                }
                updateTotalAmount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error jika terjadi masalah
            }
        });

        // Initialize views
        homeButton = findViewById(R.id.homeButton);
        menuMarket = findViewById(R.id.menumarket);
        shortVideo = findViewById(R.id.shortvidio);
        calculator = findViewById(R.id.Kalkulator);
        market = findViewById(R.id.Market);

        // Set click listeners
        homeButton.setOnClickListener(v -> navigateTo("home"));
        menuMarket.setOnClickListener(v -> navigateTo("market"));
        shortVideo.setOnClickListener(v -> navigateTo("videos"));
        calculator.setOnClickListener(v -> navigateTo("calculator"));
        market.setOnClickListener(v -> updateActiveIcon("cart"));

        // Set initial active icon
        updateActiveIcon("cart");
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
            case "market":
                intent = new Intent(this, Marketplace.class);
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

    private void initializeViews() {
        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        etNotes = findViewById(R.id.etNotes);
        btnSelectCategory = findViewById(R.id.btnSelectCategory);
        btnAddToList = findViewById(R.id.btnAddToList);
//        btnViewCalendar = findViewById(R.id.btnViewCalendar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnBoilEgg = findViewById(R.id.btnBoilEgg);
        btnSteamRice = findViewById(R.id.btnSteamRice);
        btnSteamVeg = findViewById(R.id.btnSteamVeg);
        btnBoilNoodle = findViewById(R.id.btnBoilNoodle);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        notificationSpinner = findViewById(R.id.notificationSpinner);
        hoursPicker = findViewById(R.id.hoursPicker);
        minutesPicker = findViewById(R.id.minutesPicker);
        secondsPicker = findViewById(R.id.secondsPicker);

        //konversi ukuran
        tabLayout2 = findViewById(R.id.tabLayout2);
        etInput = findViewById(R.id.etInput);
        etResult = findViewById(R.id.etResult);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        btnLihatReferensi = findViewById(R.id.btnLihatReferensi);
    }

    private void setupTabLayout2() {
        tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Clear inputs
                etInput.setText("");
                etResult.setText("");

                // Update spinners based on selected tab
                if (tab.getPosition() == 0) { // Volume
                    setupSpinnersWithValues(new ArrayList<>(volumeFactors.keySet()));
                } else { // Weight
                    setupSpinnersWithValues(new ArrayList<>(weightFactors.keySet()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    private void setupSpinners2() {
        // Initially setup with volume values
        setupSpinnersWithValues(new ArrayList<>(volumeFactors.keySet()));
    }

    private void setupSpinnersWithValues(List<String> values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, values
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }
    private void setupListeners2() {
        // Input text change listener
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                convert();
            }
        });

        // Spinner selection change listeners
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnLihatReferensi.setOnClickListener(v -> {
            // open string referensi with dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Referensi");
            builder.setMessage(R.string.referensi);
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void convert() {
        String input = etInput.getText().toString();
        if (input.isEmpty()) {
            etResult.setText("");
            return;
        }

        try {
            double value = Double.parseDouble(input);
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            Map<String, Double> factors = tabLayout.getSelectedTabPosition() == 0 ?
                    volumeFactors : weightFactors;

            // Convert to base unit first, then to target unit
            double baseValue = value * factors.get(fromUnit);
            double result = baseValue / factors.get(toUnit);

            // Format result to 4 decimal places for more precision
            etResult.setText(String.format(Locale.US, "%.3f", result));
        } catch (NumberFormatException e) {
            etResult.setText("Error");
        }
    }

    private void setupViewPager() {
        ShoppingListPagerAdapter adapter = new ShoppingListPagerAdapter(this);
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Belum Selesai"));
        tabLayout.addTab(tabLayout.newTab().setText("Selesai"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void setupListeners() {
        btnSelectCategory.setOnClickListener(v -> showCategoryDialog());
        btnAddToList.setOnClickListener(v -> addItemToList());
//        btnViewCalendar.setOnClickListener(v -> openCalendarActivity());
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.notification_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationSpinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBoilEgg.setOnClickListener(v -> setTimer(15 * 60 * 1000)); // 15 minutes
        btnSteamRice.setOnClickListener(v -> setTimer(35 * 60 * 1000)); // 35 minutes
        btnSteamVeg.setOnClickListener(v -> setTimer(10 * 60 * 1000)); // 10 minutes
        btnBoilNoodle.setOnClickListener(v -> setTimer(3 * 60 * 1000)); // 3 minutes

        btnStart.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        btnReset.setOnClickListener(v -> resetTimer());
    }
    private void setTimer(long milliseconds) {
        timeLeftInMillis = milliseconds;
        updatePickersFromTime(milliseconds);
    }

    private void updatePickersFromTime(long milliseconds) {
        int hours = (int) (milliseconds / 1000) / 3600;
        int minutes = (int) ((milliseconds / 1000) % 3600) / 60;
        int seconds = (int) (milliseconds / 1000) % 60;

        hoursPicker.setValue(hours);
        minutesPicker.setValue(minutes);
        secondsPicker.setValue(seconds);
    }
    private void updateTimeFromPickers() {
        long hours = hoursPicker.getValue();
        long minutes = minutesPicker.getValue();
        long seconds = secondsPicker.getValue();
        timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000;
    }
    private void setupNumberPickers() {
        // Setup hours picker (0-23)
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(23);
        hoursPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));

        // Setup minutes picker (0-59)
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));

        // Setup seconds picker (0-59)
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));

        // Add change listeners to update timeLeftInMillis
        NumberPicker.OnValueChangeListener listener = (picker, oldVal, newVal) -> {
            if (!isTimerRunning) {
                updateTimeFromPickers();
            }
        };

        hoursPicker.setOnValueChangedListener(listener);
        minutesPicker.setOnValueChangedListener(listener);
        secondsPicker.setOnValueChangedListener(listener);
    }
    private void startTimer() {
        if (timeLeftInMillis > 0) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updatePickersFromTime(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                    btnStart.setImageResource(R.drawable.ic_play);
                    showNotification();
                }
            }.start();

            isTimerRunning = true;
            btnStart.setImageResource(R.drawable.ic_pause);
        }
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        btnStart.setImageResource(R.drawable.ic_play);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 0;
        updatePickersFromTime(0);
        isTimerRunning = false;
        btnStart.setImageResource(R.drawable.ic_play);
    }


    private void showNotification() {
        // Implement notification logic here based on spinner selection
        Toast.makeText(this, "Timer Selesai!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    private void showCategoryDialog() {
        String[] categories = {"Daging", "Ikan", "Bahan Makanan", "Sayuran", "Buah", "Lainnya"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Kategori");

        builder.setSingleChoiceItems(categories, -1, (dialog, which) -> {
            selectedCategory = categories[which];
            btnSelectCategory.setText(selectedCategory);
            dialog.dismiss();
        });

        builder.show();
    }

    private void addItemToList() {
        String name = etItemName.getText().toString();
        String quantityStr = etQuantity.getText().toString();
        String priceStr = etPrice.getText().toString();
        String notes = etNotes.getText().toString();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty() || selectedCategory.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        double price = Double.parseDouble(priceStr);
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());

        // Create the shopping item
        ShoppingItem item;
        item = new ShoppingItem(name, quantity, price, selectedCategory, notes, currentDate);

        // Push item to Firebase
        DatabaseReference newItemRef = shoppingListRef.child("activeItems").push();
        item.setId(newItemRef.getKey());
        newItemRef.setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                activeListFragment.addItem(item);
                clearInputs();
            } else {
                Toast.makeText(this, "Gagal menambahkan item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputs() {
        etItemName.setText("");
        etQuantity.setText("");
        etPrice.setText("");
        etNotes.setText("");
        btnSelectCategory.setText("Pilih Kategori");
        selectedCategory = "";
    }

    public void updateTotalAmountAfterAddition(double amountToAdd) {
        totalCompletedAmount += amountToAdd;
        updateTotalAmount();
    }

    public void updateTotalAmountAfterDeletion(double amountToSubtract) {
        totalCompletedAmount -= amountToSubtract;
        updateTotalAmount();
    }

    private void saveTotalCompletedAmountToFirebase() {
        shoppingListRef.child("spending").setValue(totalCompletedAmount)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Gagal memperbarui data spending di Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalAmount() {
        String formattedAmount = String.format(Locale.getDefault(), "Rp %.2f", totalCompletedAmount);
        tvTotalAmount.setText(formattedAmount);

        // Simpan nilai ke Firebase
        saveTotalCompletedAmountToFirebase();
    }

    class ShoppingListPagerAdapter extends FragmentStateAdapter {
        public ShoppingListPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? activeListFragment : completedListFragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
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
