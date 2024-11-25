package com.tahhu.coba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class FoodActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private View selectedCategory = null;
    private FoodAdapter adapter;
    private Button btnCheckout;
    private List<FoodItem> cartItems = new ArrayList<>();
    private Map<String, List<FoodItem>> foodData = new HashMap<>();
    private ViewPager2 bannerViewPager;
    private BannerAdapter bannerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Inisialisasi data kategori
        initFoodData();

        recyclerView = findViewById(R.id.recyclerView);
        btnCheckout = findViewById(R.id.btn_checkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tampilkan kategori pertama sebagai default
        adapter = new FoodAdapter(foodData.get("Makanan"), item -> addToCart(item));
        recyclerView.setAdapter(adapter);

        // Listener kategori dengan perubahan warna
        View btnMakanan = findViewById(R.id.btn_makanan);
        View btnMinuman = findViewById(R.id.btn_minuman);
        View btnBuah = findViewById(R.id.btn_buah);
        View btnSnack = findViewById(R.id.btn_snack);
        ImageView btnback = findViewById(R.id.beranda);


        List<Integer> bannerImages = List.of(
                R.drawable.bannerfood,
                R.drawable.bannerfood2
        );

        // Inisialisasi ViewPager2 untuk banner
        bannerViewPager = findViewById(R.id.bannerViewPager);
        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        autoSlideBanner();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        View.OnClickListener categoryClickListener = v -> {
            // Atur ulang warna kategori sebelumnya
            if (selectedCategory != null) {
                selectedCategory.setSelected(false);
            }

            // Tandai kategori baru
            v.setSelected(true);
            selectedCategory = v;

            // Update daftar item menggunakan if-else
            if (v.getId() == R.id.btn_makanan) {
                updateList("Makanan");
            } else if (v.getId() == R.id.btn_minuman) {
                updateList("Minuman");
            } else if (v.getId() == R.id.btn_buah) {
                updateList("Camilan");
            } else if (v.getId() == R.id.btn_snack) {
                updateList("Oleh-oleh Khas");
            }
        };

        btnMakanan.setOnClickListener(categoryClickListener);
        btnMinuman.setOnClickListener(categoryClickListener);
        btnBuah.setOnClickListener(categoryClickListener);
        btnSnack.setOnClickListener(categoryClickListener);

        // Navigasi ke halaman checkout
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(FoodActivity.this, CheckoutActivityFood.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
            startActivity(intent);
        });

        // Tandai kategori awal (default)
        btnMakanan.performClick(); // Simulasikan klik untuk kategori awal
    }

    private void initFoodData() {
        foodData.put("Makanan", List.of(
                new FoodItem("Hamburger", 15000, R.drawable.hamburget, 15),
                new FoodItem("Pizza", 20000, R.drawable.pizza, 20),
                new FoodItem("HotDog", 15000, R.drawable.hotdogs, 15),
                new FoodItem("PizzaHut", 20000, R.drawable.pizzahut, 20)
        ));
        foodData.put("Minuman", List.of(
                new FoodItem("Es Teh", 5000, R.drawable.drink1, 5),
                new FoodItem("Jus Alpukat", 12000, R.drawable.drink2, 10),
                new FoodItem("Coca-cola", 5000, R.drawable.drink1, 5),
                new FoodItem("Sprite", 12000, R.drawable.drink2, 10)
        ));
        foodData.put("Camilan", List.of(
                new FoodItem("Pisang", 5000, R.drawable.camilan2, 0),
                new FoodItem("Mangga", 8000, R.drawable.camilan1, 0),
                new FoodItem("Pisang", 5000, R.drawable.camilan2, 0),
                new FoodItem("Mangga", 8000, R.drawable.camilan1, 0)
        ));
        foodData.put("Oleh-oleh Khas", List.of(
                new FoodItem("Keripik Singkong", 7000, R.drawable.khas2, 0),
                new FoodItem("Kacang Goreng", 10000, R.drawable.khas1, 0),
                new FoodItem("Singkong", 7000, R.drawable.khas2, 0),
                new FoodItem("Kacang", 10000, R.drawable.khas1, 0)
        ));
    }

    private void updateList(String category) {
        adapter.updateData(foodData.get(category));
    }

    private void addToCart(FoodItem newItem) {
        boolean itemExists = false;

        for (FoodItem item : cartItems) {
            if (item.getName().equals(newItem.getName())) {
                // Jika item sudah ada, tambahkan quantity
                item.setQuantity(item.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            // Jika item belum ada, tambahkan ke list
            newItem.setQuantity(1); // Set quantity awal jika item baru
            cartItems.add(newItem);
        }

        btnCheckout.setVisibility(View.VISIBLE); // Tampilkan tombol checkout jika ada item
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(FoodActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(FoodActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(FoodActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void autoSlideBanner() {
        final int delayMillis = 3000; // Delay antara slide
        final int[] currentItem = {0};

        bannerViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    int itemCount = bannerViewPager.getAdapter().getItemCount();
                    currentItem[0] = (currentItem[0] + 1) % itemCount;
                    bannerViewPager.setCurrentItem(currentItem[0], true);
                }
                bannerViewPager.postDelayed(this, delayMillis);
            }
        }, delayMillis);
    }

}
