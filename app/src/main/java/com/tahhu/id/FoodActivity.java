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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMakanan, recyclerViewMinuman, recyclerViewCamilan, recyclerViewKhas;
    private Button btnCheckout;
    private List<FoodItem> cartItems = new ArrayList<>();
    private Map<String, List<FoodItem>> foodData = new HashMap<>();
    private ViewPager2 bannerViewPager;
    private BannerAdapter bannerAdapter;
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Inisialisasi data makanan
        initFoodData();

        // Inisialisasi RecyclerView untuk setiap kategori
        recyclerViewMakanan = findViewById(R.id.recyclerView_makanan);
        recyclerViewMinuman = findViewById(R.id.recyclerView_minuman);
        recyclerViewCamilan = findViewById(R.id.recyclerView_camilan);
        recyclerViewKhas = findViewById(R.id.recyclerView_khas);

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

        // Atur RecyclerView dengan LinearLayoutManager dan adapter
        setupRecyclerView(recyclerViewMakanan, "Makanan");
        setupRecyclerView(recyclerViewMinuman, "Minuman");
        setupRecyclerView(recyclerViewCamilan, "Camilan");
        setupRecyclerView(recyclerViewKhas, "Oleh-oleh Khas");

        // Tombol checkout
        btnCheckout = findViewById(R.id.btn_checkout);
        btnCheckout.setVisibility(View.GONE);  // Hide checkout button initially
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, CheckoutActivityFood.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
            startActivity(intent);
        });

        // Inisialisasi ViewPager2 untuk banner
        bannerViewPager = findViewById(R.id.bannerViewPager);
        List<Integer> bannerImages = List.of(
                R.drawable.bannerfood,
                R.drawable.bannerfood2
        );
        bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        autoSlideBanner();

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.beranda);
        btnBack.setOnClickListener(v -> startActivity(new Intent(FoodActivity.this, MainActivity.class)));

        // Tombol menu cart
        ImageView btn_cart = findViewById(R.id.btn_cart);
        btn_cart.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, CheckoutActivityFood.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
            startActivity(intent);
        });

    }

    private void initFoodData() {
        foodData.put("Makanan", List.of(
                new FoodItem("Hamburger", 15000, R.drawable.hamburget, 15),
                new FoodItem("Pizza", 20000, R.drawable.pizza, 20),
                new FoodItem("HotDog", 15000, R.drawable.hotdogs, 15)
        ));
        foodData.put("Minuman", List.of(
                new FoodItem("Es Teh", 5000, R.drawable.drink1, 5),
                new FoodItem("Jus Alpukat", 12000, R.drawable.drink2, 10),
                new FoodItem("Es Teh Jumbo", 5000, R.drawable.drink1, 5),
                new FoodItem("Jus Alpukat Kocok", 12000, R.drawable.drink2, 10)
        ));
        foodData.put("Camilan", List.of(
                new FoodItem("Pisang", 5000, R.drawable.camilan1, 0),
                new FoodItem("Mangga", 8000, R.drawable.camilan2, 0)
        ));
        foodData.put("Oleh-oleh Khas", List.of(
                new FoodItem("Keripik Singkong", 7000, R.drawable.kacang, 0),
                new FoodItem("Kacang Goreng", 10000, R.drawable.khas2, 0)
        ));
    }

    private void setupRecyclerView(RecyclerView recyclerView, String category) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FoodAdapter adapter = new FoodAdapter(foodData.get(category), this::addToCart, this);
        recyclerView.setAdapter(adapter);
    }

    private void addToCart(FoodItem newItem) {
        boolean itemExists = false;

        // Check if the item is already in the cart, if so, increment the quantity
        for (FoodItem item : cartItems) {
            if (item.getName().equals(newItem.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        // If the item is not already in the cart, add it
        if (!itemExists) {
            newItem.setQuantity(1); // Set the initial quantity to 1
            cartItems.add(newItem);
        }

        // Show the checkout button when an item is added
        btnCheckout.setVisibility(View.VISIBLE);
    }

    private void autoSlideBanner() {
        final int delayMillis = 3000; // Waktu delay antar slide
        bannerViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    int itemCount = bannerViewPager.getAdapter().getItemCount();
                    int currentItem = bannerViewPager.getCurrentItem();
                    bannerViewPager.setCurrentItem((currentItem + 1) % itemCount, true);
                }
                bannerViewPager.postDelayed(this, delayMillis);
            }
        }, delayMillis);
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
