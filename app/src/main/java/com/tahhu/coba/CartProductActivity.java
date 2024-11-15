package com.tahhu.coba;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RadioGroup;
import android.widget.Button;
import android.content.Intent;

import java.util.List;

public class CartProductActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private TextView totalQuantityView, totalPriceView;
    private CartProductAdapter cartProductAdapter;
    private List<CartProduct> cartProductList;  // Daftar CartProduct, bukan Product
    private double totalPrice;
    private int totalQuantity;
    private RadioGroup radioGroupShipping;
    private int shippingCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_product);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalQuantityView = findViewById(R.id.totalQuantityView);
        totalPriceView = findViewById(R.id.totalPriceView);
        ImageView btnBack = findViewById(R.id.back_to_marketplace);
        radioGroupShipping = findViewById(R.id.radioGroupShipping);
        TextView ongkirPriceView = findViewById(R.id.ongkirPriceView);

        cartProductList = CartManager.getInstance().getCartProducts();
        cartProductAdapter = new CartProductAdapter(cartProductList, this::updateTotals);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartProductAdapter);

        btnBack.setOnClickListener(v -> finish());

        Button btnPayment = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(v -> {
            Intent intent = new Intent(CartProductActivity.this, PaymentActivity.class);
            // Kirim data totalPrice ke PaymentActivity
            intent.putExtra("totalPrice", totalPrice);
            startActivity(intent);
        });

        radioGroupShipping.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_standard_shipping) {
                shippingCost = 10000;
            } else if (checkedId == R.id.rb_express_shipping) {
                shippingCost = 20000;
            } else if (checkedId == R.id.rb_nextday_shipping) {
                shippingCost = 30000;
            }

            ongkirPriceView.setText("Rp " + shippingCost);

            updateTotals();
        });


        updateTotals();
    }
    private void updateTotals() {
        totalQuantity = 0;
        totalPrice = 0.0;

        // Iterasi melalui cartProductList yang berisi CartProduct
        for (CartProduct cartProduct : cartProductList) {
            Product product = cartProduct.getProduct();
            totalQuantity += cartProduct.getQuantity();

            // Menghapus "Rp" dan koma sebelum konversi harga
            String priceString = product.getPrice().replace("Rp", "").replace(",", "").trim();
            double price = Double.parseDouble(priceString);

            totalPrice += cartProduct.getQuantity() * price;
        }

        totalPrice += shippingCost;

        totalQuantityView.setText(String.valueOf(totalQuantity));
        totalPriceView.setText("Rp " + String.format("%,.2f", totalPrice)); // Format harga agar lebih rapi
    }
}
