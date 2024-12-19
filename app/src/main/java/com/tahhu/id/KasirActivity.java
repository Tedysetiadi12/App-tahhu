package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KasirActivity extends AppCompatActivity implements ProductAdapterKasir.OnProductClickListener, CartAdapterKasir.OnCartItemChangeListener {
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private List<ProductKasir> products = new ArrayList<>();
    private List<CartItemKasir> cartItems = new ArrayList<>();
    private double total = 0.0;
    private RecyclerView rvProducts;
    private RecyclerView rvCart;
    private ProductAdapterKasir productAdapterKasir;
    private CartAdapterKasir cartAdapterKasir;
    private TextView tvTotal;
    private String userId;
    private EditText etPaymentAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kasir);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(KasirActivity.this, LoginActivity.class));
            finish();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Kasir");

        rvProducts = findViewById(R.id.rvProducts);
        rvCart = findViewById(R.id.rvCart);
        EditText etPaymentAmount = findViewById(R.id.etPaymentAmount);
        tvTotal = findViewById(R.id.tvTotal);
        TextView tvChange = findViewById(R.id.tvChange);
        Button btnPay = findViewById(R.id.btnPay);
        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button statistiKkasir = findViewById(R.id.statistikKasir);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapterKasir = new ProductAdapterKasir(products, this);
        rvProducts.setAdapter(productAdapterKasir);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapterKasir = new CartAdapterKasir(cartItems, this);
        rvCart.setAdapter(cartAdapterKasir);

        loadProducts();

        // TextWatcher untuk menghitung perubahan pada etPaymentAmount
        etPaymentAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String amountStr = charSequence.toString();
                if (!amountStr.isEmpty()) {
                    double amount = Double.parseDouble(amountStr);
                    double change = amount - total;
                    tvChange.setText(String.format("Rp %.0f", change));
                } else {
                    tvChange.setText("Rp 0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btnPay.setOnClickListener(v -> {
            String amountStr = etPaymentAmount.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                double change = amount - total;
                if (change >= 0) {
                    saveTransaction(amount, change);
                    updateProductStock(); // Update stok produk setelah pembayaran
                    cartItems.clear(); // Reset keranjang
                    updateCart();
                    etPaymentAmount.setText("");
                    tvChange.setText("Rp 0");
                } else {
                    Toast.makeText(this, "Jumlah pembayaran kurang", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Masukkan jumlah pembayaran", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddProduct.setOnClickListener(v -> {
            showAddProductDialog();
        });

        statistiKkasir.setOnClickListener(v -> {
            startActivity(new Intent(this, StatistikKasirActivity.class));
        });

    }

    private void loadProducts() {
        database.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductKasir product = snapshot.getValue(ProductKasir.class);
                    if (product != null) {
                        product.setId(snapshot.getKey());
                        products.add(product);
                    }
                }
                productAdapterKasir.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(KasirActivity.this, "Failed to load products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCart() {
        total = 0;
        for (CartItemKasir item : cartItems) {
            total += item.getSubtotal();
        }
        tvTotal.setText(String.format(Locale.getDefault(), "Rp %.0f", total));
        cartAdapterKasir.updateCartItems(cartItems); // Use the new update method
    }


    private void saveTransaction(double amountPaid, double change) {
        DatabaseReference transRef = database.child("transactions").push();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> transactionData = new HashMap<>();
            transactionData.put("userId", userId);
            transactionData.put("timestamp", ServerValue.TIMESTAMP);
            transactionData.put("total", total);
            transactionData.put("amountPaid", amountPaid);
            transactionData.put("change", change);

            List<Map<String, Object>> itemsList = new ArrayList<>();
            for (CartItemKasir item : cartItems) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("productId", item.getProduct().getId());
                itemMap.put("productName", item.getProduct().getName());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("price", item.getProduct().getPrice());
                itemMap.put("subtotal", item.getSubtotal());
                itemsList.add(itemMap);
            }
            transactionData.put("items", itemsList);

            transRef.setValue(transactionData)
                    .addOnSuccessListener(aVoid -> Toast.makeText(KasirActivity.this, "Transaction saved successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(KasirActivity.this, "Failed to save transaction: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProductStock() {
        for (CartItemKasir item : cartItems) {
            ProductKasir product = item.getProduct();
            int newStock = product.getStock() - item.getQuantity();
            database.child("products").child(product.getId()).child("stock").setValue(newStock);
        }
    }


    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        final EditText etProductName = dialogView.findViewById(R.id.etProductName);
        final EditText etProductPrice = dialogView.findViewById(R.id.etProductPrice);
        final EditText etProductStock = dialogView.findViewById(R.id.etProductStock);

        builder.setTitle("Tambah Produk Baru")
                .setPositiveButton("Tambah", (dialog, id) -> {
                    String name = etProductName.getText().toString().trim();
                    String priceStr = etProductPrice.getText().toString().trim();
                    String stockStr = etProductStock.getText().toString().trim();

                    if (!name.isEmpty() && !priceStr.isEmpty() && !stockStr.isEmpty()) {
                        double price = Double.parseDouble(priceStr);
                        int stock = Integer.parseInt(stockStr);
                        addNewProduct(name, price, stock);
                    } else {
                        Toast.makeText(KasirActivity.this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Batal", (dialog, id) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addNewProduct(String name, double price, int stock) {
        DatabaseReference productsRef = database.child("products");
        String productId = productsRef.push().getKey();
        ProductKasir newProduct = new ProductKasir(productId, name, price, stock);

        productsRef.child(productId).setValue(newProduct)
                .addOnSuccessListener(aVoid -> Toast.makeText(KasirActivity.this, "Produk berhasil ditambahkan", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(KasirActivity.this, "Gagal menambahkan produk: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAddToCartClick(ProductKasir product) {
        CartItemKasir existingItem = findCartItemByProduct(product);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + 1;
            if (newQuantity <= product.getStock()) {
                existingItem.setQuantity(newQuantity);
                updateCart();
            } else {
                Toast.makeText(this, "Stock tidak mencukupi", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (product.getStock() > 0) {
                cartItems.add(new CartItemKasir(product, 1));
                updateCart();
            } else {
                Toast.makeText(this, "Produk tidak tersedia", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onQuantityChange(CartItemKasir item, int newQuantity) {
        if (newQuantity > 0 && newQuantity <= item.getProduct().getStock()) {
            item.setQuantity(newQuantity);
            updateCart();
        } else if (newQuantity > item.getProduct().getStock()) {
            Toast.makeText(this, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Quantity harus lebih dari 0", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRemoveFromCart(CartItemKasir item) {
        cartItems.remove(item);
        updateCart();
    }

    private CartItemKasir findCartItemByProduct(ProductKasir product) {
        for (CartItemKasir item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                return item;
            }
        }
        return null;
    }
}

