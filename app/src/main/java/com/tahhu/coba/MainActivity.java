package com.tahhu.coba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Button btninternet;
    public ImageView btn_radio,btn_market, btn_finence, btn_ride, btn_survey, btn_uco ,menu_market, menu_finance, menu_ride;

    private HorizontalScrollView horizontalScrollView;
    private Handler handler = new Handler();
    private int scrollPosition = 0;
    private int scrollStep = 3; // Kecepatan scroll, semakin besar semakin cepat
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize the ImageView

        btninternet = findViewById(R.id.btn_internet);
        btn_finence = findViewById(R.id.financeIcon);
        btn_market = findViewById(R.id.marketIcon);
        btn_ride = findViewById(R.id.rideIcon);
        btn_survey = findViewById(R.id.surveyIcon);
        btn_uco = findViewById(R.id.ucoIcon);
        menu_market = findViewById(R.id.menumarket);
        menu_finance = findViewById(R.id.menufavorite);
        menu_ride = findViewById(R.id.menuride);
        btn_radio = findViewById(R.id.streaming);
        TextView btn_all = findViewById(R.id.all);

        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Deskripsi produk (sama untuk semua produk)
        String productDescription = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Lampu Stan", "Rp 50,000", R.drawable.product1, "4.5", "120 sold", productDescription));
        products.add(new Product("Kipas Angin", "Rp 22,000", R.drawable.product2, "4.7", "12k sold", productDescription));
        products.add(new Product("Sepatu Slim", "Rp 120,000", R.drawable.product4, "4.5", "12k sold", productDescription));
        products.add(new Product("Sepatu RDS", "Rp 420,000", R.drawable.product3, "4.8", "6k sold", productDescription));
        products.add(new Product("Lampu RS", "Rp 420,000", R.drawable.product1, "4.3", "134 sold", productDescription));
        products.add(new Product("Kipas SSS", "Rp 420,000", R.drawable.product2, "4.7", "12k sold", productDescription));


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
        btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Survey.class);
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
        menu_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, Marketplace.class);
                startActivity(intent);
            }
        });
        menu_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, ShortVidio.class);
                startActivity(intent);
            }
        });
        menu_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event, for example, open another activity
                Intent intent = new Intent(MainActivity.this, rideSharing.class);
                startActivity(intent);
            }
        });

        horizontalScrollView = findViewById(R.id.horizontalScrollView);
//        startAutoScroll();
    }
    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollPosition += scrollStep;
                horizontalScrollView.scrollTo(scrollPosition, 0);

                // Reset scroll position jika sudah mencapai akhir
                if (scrollPosition >= horizontalScrollView.getChildAt(0).getWidth() - horizontalScrollView.getWidth()) {
                    scrollPosition = 0;
                }

                // Ulangi setiap 20 milidetik untuk efek smooth scrolling
                handler.postDelayed(this, 20);
            }
        }, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Hentikan auto scroll saat activity ditutup
    }
}