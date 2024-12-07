package com.tahhu.coba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Button btninternet,btn_test;
    FloatingActionButton btnmarket ;
    public ImageView btn_radio,btn_market, btn_finence, btn_ride,btn_cctv, btn_uco ,menu_market, menu_user, menu_ride,
            btn_tv, btn_food, btn_security, btn_homeButton;
    private ProgressBar progressBar2;
    private BannerAdapter bannerAdapter;
    ViewPager2 viewPager3, viewPager4;
    private FrameLayout navHome, navSearch, navFavorite, navSettings;
    private FrameLayout[] navItems;
    private int selectedItem = 0;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the ImageView
        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navFavorite = findViewById(R.id.nav_favorite);
        navSettings = findViewById(R.id.nav_settings);
        navItems = new FrameLayout[]{navHome, navSearch, navFavorite, navSettings};

        for (int i = 0; i < navItems.length; i++) {
            final int index = i;
            navItems[i].setOnClickListener(v -> selectItem(index));
        }

        // Initialize the first item as selected
        selectItem(0);
        btninternet = findViewById(R.id.btn_internet);
        btn_test = findViewById(R.id.btn_test);
        btn_finence = findViewById(R.id.financeIcon);
        btn_market = findViewById(R.id.marketIcon);
        btn_ride = findViewById(R.id.rideIcon);
        btn_uco = findViewById(R.id.ucoIcon);

        btn_radio = findViewById(R.id.streaming);
        btn_tv = findViewById(R.id.tvIcon);
        btn_cctv = findViewById(R.id.cctvicon);
        btn_food = findViewById(R.id.foodIcon);
        btn_security = findViewById(R.id.securityIcon);
        btn_homeButton = findViewById(R.id.homeButton);
        TextView btn_all = findViewById(R.id.all);


        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        progressBar2 = findViewById(R.id.progressBar2);

        // Membuat data untuk slide
        List<SlideAdapter.SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideAdapter.SlideItem(R.drawable.banner_minyak, "Slide 1","https://tahhu.com"));
        slideItems.add(new SlideAdapter.SlideItem(R.drawable.benner2, "Slide 2","https://tahhu.com"));
        slideItems.add(new SlideAdapter.SlideItem(R.drawable.bener1, "Slide 3","https://tahhu.com"));
        // Set adapter ke ViewPager2

        // Buat adapter dan pasang listener
        SlideAdapter adapterslid = new SlideAdapter(slideItems, (position, url) -> {
            // Aksi saat banner diklik, buka URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        viewPager2.setAdapter(adapterslid);

        viewPager3 = findViewById(R.id.viewPager3);
        List<SlideAdapter.SlideItem> bannerItems = new ArrayList<>();
        bannerItems.add(new SlideAdapter.SlideItem(R.drawable.diskon1, "Diskon 1","https://tahhu.com"));
        bannerItems.add(new SlideAdapter.SlideItem(R.drawable.diskon2, "Diskon 2","https://tahhu.com"));
        bannerItems.add(new SlideAdapter.SlideItem(R.drawable.diskon3, "Diskon 3","https://tahhu.com"));
        // Buat adapter dan pasang listener
        SlideAdapter bannerAdapter = new SlideAdapter(bannerItems, (position, url) -> {
            // Aksi saat banner diklik, buka URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        viewPager3.setAdapter(bannerAdapter);

        viewPager4 = findViewById(R.id.viewPager4);
        List<SlideAdapter.SlideItem> bannerItem2 = new ArrayList<>();
        bannerItem2.add(new SlideAdapter.SlideItem(R.drawable.speedtest, "Test Speed","https://tahhu.com/speed"));

        // Buat adapter dan pasang listener
        SlideAdapter bannerAdapter1 = new SlideAdapter(bannerItem2, (position, url) -> {
            // Aksi saat banner diklik, buka URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
// Set adapter pada ViewPager2
        viewPager4.setAdapter(bannerAdapter1);

        // Mulai auto scroll
        autoSlideBanner();

        // Menghubungkan ViewPager2 dengan TabLayout
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            // Anda dapat menambahkan teks atau icon jika diperlukan
            // tab.setText("Tab " + (position + 1)); // Contoh jika Anda ingin menambahkan teks
        }).attach();

        // Menampilkan ProgressBar saat data sedang diproses (misalnya, selama pengambilan data)
        showProgressBar(true); // Menampilkan ProgressBar

        // Simulasi pengambilan data atau proses lain
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false); // Menyembunyikan ProgressBar setelah proses selesai
            }
        }, 1000); // Simulasi selesai setelah 2 detik


        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Deskripsi produk (sama untuk semua produk)
        String productDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

        List<Product> products = new ArrayList<>();
        products.add(new Product("TAS UQIXO WANITA BAHAN LEBUT", "Rp 420,000", R.drawable.tas, "4.3", "134 sold", productDescription));
        products.add(new Product("BAJU CROP CEWEK BERBAGAI UKURAN M XL", "Rp 123,000", R.drawable.baju, "4.7", "12k sold", productDescription));
        products.add(new Product("LAMPU STAN MURAH DAN AWET", "Rp 50,000", R.drawable.product1, "4.5", "120 sold", productDescription));
        products.add(new Product("KIPAS ANGIN PLATINUM ", "Rp 22,000", R.drawable.product2, "4.7", "12k sold", productDescription));
        products.add(new Product("SEPATU SLIM PRIA ", "Rp 120,000", R.drawable.product4, "4.5", "12k sold", productDescription));
        products.add(new Product("SEPATU DESASS BAHAN KULIT", "Rp 420,000", R.drawable.product3, "4.8", "6k sold", productDescription));


        ProductAdapter adapter = new ProductAdapter(this, products);
        productRecyclerView.setAdapter(adapter);
        // Set an OnClickListener
        btninternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://11.15.0.1";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://tahhu.com/speed";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
        btn_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, RadioStreamin.class);
                startActivity(intent);
            }
        });
        btn_cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, cctv.class);
                startActivity(intent);
            }
        });
        btn_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
        btn_finence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, finance.class);
                startActivity(intent);
            }
        });
        btn_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, rideSharing.class);
                startActivity(intent);
            }
        });

        btn_uco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, UcoActivity.class);
                startActivity(intent);
            }
        });

        btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, TVStreamingActivity.class);
                startActivity(intent);
            }
        });

        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                startActivity(intent);
            }
        });

        btn_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, SecurityActivity.class);
                startActivity(intent);
            }
        });
<<<<<<< HEAD

        menu_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
        menu_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, UserSettingActivity.class);
                startActivity(intent);
            }
        });
        menu_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, ShortVidio.class);
                startActivity(intent);
            }
        });
        btn_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, CalenderActivity.class);
                startActivity(intent);
            }
        });


=======
>>>>>>> c2f2309471fca081fb4604356afa481f1a3a43e4
    }

    private void selectItem(int index) {
        // Deselect the previously selected item
        navItems[selectedItem].setSelected(false);

        // Select the new item
        navItems[index].setSelected(true);
        selectedItem = index;

        if (index == 0) {
            // Home (current activity)
            // Do nothing as we're already in MainActivity
        } else if (index == 1) {
            // Search
            Intent searchIntent = new Intent(this, Marketplace.class);
            startActivity(searchIntent);
        } else if (index == 2) {
            // Favorite
            Intent favoriteIntent = new Intent(this, ShortVidio.class);
            startActivity(favoriteIntent);
        } else if (index == 3) {
            // Settings
            Intent settingsIntent = new Intent(this, UserSettingActivity.class);
            startActivity(settingsIntent);
        }
        // Here you would typically change the content based on the selected item
        // For example, replace fragments or update the UI
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar2.setVisibility(View.VISIBLE);
        } else {
            progressBar2.setVisibility(View.GONE);
        }
    }
    private void autoSlideBanner() {
        final int delayMillis = 3000; // Waktu delay antar slide
        viewPager3.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPager3.getAdapter() != null) {
                    int itemCount = viewPager3.getAdapter().getItemCount();
                    int currentItem = viewPager3.getCurrentItem();
                    viewPager3.setCurrentItem((currentItem + 1) % itemCount, true);
                }
                viewPager3.postDelayed(this, delayMillis);
            }
        }, delayMillis);
    }
}