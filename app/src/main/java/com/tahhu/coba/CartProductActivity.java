package com.tahhu.coba;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartProductActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private TextView totalQuantityView, totalPriceView;
    private CartProductAdapter cartProductAdapter;
    private List<CartProduct> cartProductList;  // Daftar CartProduct, bukan Product
    private double totalPrice;
    private int totalQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalQuantityView = findViewById(R.id.totalQuantityView);
        totalPriceView = findViewById(R.id.totalPriceView);
        ImageView btnBack = findViewById(R.id.back_to_marketplace);

        cartProductList = CartManager.getInstance().getCartProducts();  // Mengambil CartProduct, bukan Product
        cartProductAdapter = new CartProductAdapter(cartProductList, this::updateTotals);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartProductAdapter);

        btnBack.setOnClickListener(v -> finish());

        updateTotals();
    }

    private void updateTotals() {
        totalQuantity = 0;
        totalPrice = 0.0;

        // Iterasi melalui cartProductList yang berisi CartProduct
        for (CartProduct cartProduct : cartProductList) {
            Product product = cartProduct.getProduct();  // Ambil Product dari CartProduct
            totalQuantity += cartProduct.getQuantity();  // Gunakan quantity dari CartProduct

            // Menghapus "Rp" dan koma (",") sebelum konversi harga
            String priceString = product.getPrice().replace("Rp", "").replace(",", "").trim();

            // Mengonversi harga menjadi double
            double price = Double.parseDouble(priceString);

            totalPrice += cartProduct.getQuantity() * price;  // Harga dari Product
        }

        totalQuantityView.setText(String.valueOf(totalQuantity));
        totalPriceView.setText("Rp " + totalPrice);
    }
}
