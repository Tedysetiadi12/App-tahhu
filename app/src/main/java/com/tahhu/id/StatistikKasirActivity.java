package com.tahhu.id;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatistikKasirActivity extends AppCompatActivity {
    private DatabaseReference database;
    private String userId;
    private Calendar currentMonth;
    private TextView tvMonthYear, tvTotalSales;
    private RecyclerView rvBestSellers, rvTransactionHistory;
    private BestSellerAdapter bestSellerAdapter;
    private TransactionHistoryAdapter transactionAdapter;
    private SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy", new Locale("id"));
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik_kasir);

        initializeViews();
        setupFirebase();
        setupRecyclerViews();
        setupMonthNavigation();
        loadData();
    }

    private void initializeViews() {
        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvTotalSales = findViewById(R.id.tvTotalSales);
        rvBestSellers = findViewById(R.id.rvBestSellers);
        rvTransactionHistory = findViewById(R.id.rvTransactionHistory);

        currentMonth = Calendar.getInstance();
        updateMonthYearDisplay();

        findViewById(R.id.btnPrevMonth).setOnClickListener(v -> navigateMonth(-1));
        findViewById(R.id.btnNextMonth).setOnClickListener(v -> navigateMonth(1));
    }

    private void setupFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Kasir");
    }

    private void setupRecyclerViews() {
        bestSellerAdapter = new BestSellerAdapter();
        rvBestSellers.setLayoutManager(new LinearLayoutManager(this));
        rvBestSellers.setAdapter(bestSellerAdapter);

        transactionAdapter = new TransactionHistoryAdapter(this::onTransactionComplete);
        rvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));
        rvTransactionHistory.setAdapter(transactionAdapter);
    }

    private void setupMonthNavigation() {
        currentMonth = Calendar.getInstance();
        updateMonthYearDisplay();
    }

    private void navigateMonth(int months) {
        currentMonth.add(Calendar.MONTH, months);
        updateMonthYearDisplay();
        loadData();
    }

    private void updateMonthYearDisplay() {
        tvMonthYear.setText(monthYearFormat.format(currentMonth.getTime()));
    }

    private void loadData() {
        String monthKey = dateFormat.format(currentMonth.getTime());

        // Load transactions for the month
        database.child("transactions")
                .orderByChild("timestamp")
                .startAt(getMonthStart())
                .endAt(getMonthEnd())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Transaction> transactions = new ArrayList<>();
                        double totalSales = 0;
                        Map<String, ProductSales> productSalesMap = new HashMap<>();

                        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                            Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                            if (transaction != null) {
                                transactions.add(transaction);
                                totalSales += transaction.getTotal();

                                // Calculate product sales
                                for (TransactionItem item : transaction.getItems()) {
                                    ProductSales sales = productSalesMap.getOrDefault(
                                            item.getProductId(),
                                            new ProductSales(item.getProductName())
                                    );
                                    sales.addQuantity(item.getQuantity());
                                    productSalesMap.put(item.getProductId(), sales);
                                }
                            }
                        }

                        // Update UI
                        tvTotalSales.setText(String.format(Locale.getDefault(), "Rp %,.0f", totalSales));

                        // Sort and display best sellers
                        List<ProductSales> bestSellers = new ArrayList<>(productSalesMap.values());
                        Collections.sort(bestSellers, (a, b) -> b.getQuantity() - a.getQuantity());
                        bestSellerAdapter.setItems(bestSellers);

                        // Display transactions
                        Collections.sort(transactions, (a, b) ->
                                Long.compare(b.getTimestamp(), a.getTimestamp()));
                        transactionAdapter.setTransactions(transactions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StatistikKasirActivity.this,
                                "Error loading data: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private long getMonthStart() {
        Calendar cal = (Calendar) currentMonth.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private long getMonthEnd() {
        Calendar cal = (Calendar) currentMonth.clone();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    private void onTransactionComplete(String transactionId) {
        database.child("transactions").child(transactionId).child("status")
                .setValue("completed")
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Transaksi selesai", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal mengupdate status: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}
