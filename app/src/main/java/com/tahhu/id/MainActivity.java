package com.tahhu.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    public Button btninternet,btn_test;
    FloatingActionButton btnmarket ;
    public ImageView btn_radio,btn_market, btn_finence, btn_ride,btn_cctv, btn_uco ,menu_market, menu_kalkulator, menu_ride,
                btn_tv, btn_food, btn_security, btn_homeButton;
    private ProgressBar progressBar2;
    private BannerAdapter bannerAdapter;
    private TextView welcomeTextView;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    private MaterialButton signOutButton;

    ViewPager2 viewPager3, viewPager4;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btninternet = findViewById(R.id.btn_internet);
        btn_test = findViewById(R.id.btn_test);
        btn_finence = findViewById(R.id.financeIcon);
        btn_market = findViewById(R.id.MarketIcon);
        btn_ride = findViewById(R.id.rideIcon);
        btn_uco = findViewById(R.id.ucoIcon);
        btn_radio = findViewById(R.id.streaming);
        btn_tv = findViewById(R.id.tvIcon);
        btn_cctv = findViewById(R.id.cctvicon);
        btn_food = findViewById(R.id.foodIcon);
        btn_security = findViewById(R.id.securityIcon);

        btn_homeButton = findViewById(R.id.homeButton);
        menu_ride = findViewById(R.id.shortvidio);
        menu_market = findViewById(R.id.menumarket);
        btnmarket = findViewById(R.id.Market);
        menu_kalkulator = findViewById(R.id.Kalkulator);

        TextView btn_all = findViewById(R.id.all);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        progressBar2 = findViewById(R.id.progressBar2);

        // Inisialisasi FirebaseAuth dan DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        welcomeTextView = findViewById(R.id.welcomeTextView);
        ImageView profileImageView = findViewById(R.id.profileImageView);

        // Cek apakah user sudah login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Ambil UID pengguna yang login
            String userId = currentUser.getUid();

            // Ambil data pengguna dari Realtime Database
            mDatabase.child("users").child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        // Ambil data username dari snapshot
                        String username = dataSnapshot.child("username").getValue(String.class);

                        // Tampilkan username di TextView
                        welcomeTextView.setText("Welcome, " + username);
                    } else {
                        Toast.makeText(MainActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Jika pengguna belum login, arahkan ke halaman login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


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

        menu_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                updateMenuIcons("market");
                Intent intent = new Intent(MainActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
        menu_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                updateMenuIcons("videos");
                Intent intent = new Intent(MainActivity.this, ShortVidio.class);
                startActivity(intent);
            }
        });
        btn_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                updateMenuIcons("home");
            }
        });

        btnmarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenuIcons("shoppinglist");
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
                startActivity(intent);
            }
        });

        ImageView menu_kalkulator = findViewById(R.id.Kalkulator);
        menu_kalkulator.setOnClickListener(v -> {
            updateMenuIcons("calculator");
            // Memanggil dialog
            DiscountCalculatorDialog dialog = new DiscountCalculatorDialog();
            dialog.show(getSupportFragmentManager(), "DiscountCalculatorDialog");
        });
        updateMenuIcons("home");

        profileImageView.setOnClickListener(v -> showProfilePopup(v));

        ImageView calenderSpending = findViewById(R.id.calenderSpending);
        calenderSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, CalendarSpendingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateMenuIcons(String activePage) {
        int activeColor = getResources().getColor(R.color.white);
        int inactiveColor = getResources().getColor(R.color.inactive_icon);

        btn_homeButton.setColorFilter(activePage.equals("home") ? activeColor : inactiveColor);
        menu_market.setColorFilter(activePage.equals("market") ? activeColor : inactiveColor);
        menu_ride.setColorFilter(activePage.equals("videos") ? activeColor : inactiveColor);
        menu_kalkulator.setColorFilter(activePage.equals("calculator") ? activeColor : inactiveColor);
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
    private void showProfilePopup(View anchor) {
        // Menampilkan popup menu
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_profile, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));

        // Dapatkan mPopup dari PopupMenu untuk mengakses PopupMenuHelper
        try {
            Field mPopupField = popupMenu.getClass().getDeclaredField("mPopup");
            mPopupField.setAccessible(true);
            Object mPopup = mPopupField.get(popupMenu);

            // Ubah background menjadi putih
            Class<?> classPopupMenu = Class.forName(mPopup.getClass().getName());
            Method setBackgroundDrawable = classPopupMenu.getDeclaredMethod("setBackgroundDrawable", Drawable.class);
            setBackgroundDrawable.setAccessible(true);

            // Terapkan background dari drawable
            Drawable whiteBackground = getResources().getDrawable(R.drawable.popup_background, null);
            setBackgroundDrawable.invoke(mPopup, whiteBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if present
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_profile) {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, UserSettingActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            mAuth.signOut();  // Sign out from Firebase
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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