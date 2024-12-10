package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMakanan, recyclerViewMinuman, recyclerViewCamilan, recyclerViewKhas;
    private Button btnCheckout;
    private List<FoodItem> cartItems = new ArrayList<>();
    private Map<String, List<FoodItem>> foodData = new HashMap<>();
    private ViewPager2 bannerViewPager;
    private BannerAdapter bannerAdapter;

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
}
