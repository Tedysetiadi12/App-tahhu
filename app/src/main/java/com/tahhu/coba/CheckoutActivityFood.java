package com.tahhu.coba;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivityFood extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckoutAdapterFood adapter;
    private TextView tvTotal;
    private List<FoodItem> cartItems;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_food);

        recyclerView = findViewById(R.id.recyclerView_checkout);
        ImageView back = findViewById(R.id.imageViewBack);
        tvTotal = findViewById(R.id.tv_total);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        calculateTotal();

        adapter = new CheckoutAdapterFood(cartItems, new CheckoutAdapterFood.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange() {
                calculateTotal();
            }
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_payment).setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivityFood.this, PaymentActivityFood.class);
            intent.putParcelableArrayListExtra("finalItems", new ArrayList<>(cartItems));
            intent.putExtra("finalTotal", totalPrice);
            startActivity(intent);
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FoodActivity.class));
            }
        });
    }

    private void calculateTotal() {
        totalPrice = 0;

        if (cartItems.isEmpty()) {
            tvTotal.setText("Your cart is empty");
            findViewById(R.id.btn_payment).setEnabled(false);
        } else {
            for (FoodItem item : cartItems) {
                totalPrice += item.getPrice() * item.getQuantity(); // Pertimbangkan quantity
            }
            tvTotal.setText("Total: Rp " + totalPrice);
            findViewById(R.id.btn_payment).setEnabled(true);
        }
    }


}
