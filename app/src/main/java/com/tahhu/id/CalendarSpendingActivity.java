package com.tahhu.id;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class CalendarSpendingActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView tvMonthlyTotal;
    private RecyclerView rvDailySpending;
    private DatabaseReference spendingRef;
    private String userId;
    private ImageView btnBack;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    private DailySpendingAdapter adapter;
    private ArrayList<ShoppingItem> dailySpendingItems = new ArrayList<>();
    private Calendar currentDisplayedMonth = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_spending);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        spendingRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("shopping_list");

        initializeViews();
        setupCalendar();
        setupRecyclerView();
        loadMonthlySpendingAndItems(currentDisplayedMonth.get(Calendar.YEAR), currentDisplayedMonth.get(Calendar.MONTH));

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal);
        rvDailySpending = findViewById(R.id.rvDailySpending);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            if (currentDisplayedMonth.get(Calendar.MONTH) != month ||
                    currentDisplayedMonth.get(Calendar.YEAR) != year) {
                currentDisplayedMonth.set(Calendar.YEAR, year);
                currentDisplayedMonth.set(Calendar.MONTH, month);
                loadMonthlySpendingAndItems(year, month);
            }

            loadDailySpending(dateFormat.format(selectedDate.getTime()));
        });
    }

    private void setupRecyclerView() {
        adapter = new DailySpendingAdapter(dailySpendingItems);
        rvDailySpending.setLayoutManager(new LinearLayoutManager(this));
        rvDailySpending.setAdapter(adapter);
    }

    private void loadMonthlySpendingAndItems(int year, int month) {
        spendingRef.child("completedItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double monthlyTotal = 0;
                dailySpendingItems.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);
                    if (item != null) {
                        try {
                            Date itemDate = dateFormat.parse(item.getDate());
                            Calendar itemCalendar = Calendar.getInstance();
                            itemCalendar.setTime(itemDate);

                            if (itemCalendar.get(Calendar.MONTH) == month &&
                                    itemCalendar.get(Calendar.YEAR) == year) {
                                monthlyTotal += (item.getPrice() * item.getQuantity());
                                dailySpendingItems.add(item);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Collections.sort(dailySpendingItems, (item1, item2) -> item1.getDate().compareTo(item2.getDate()));
                adapter.notifyDataSetChanged();

                // Update monthly total in UI
                tvMonthlyTotal.setText(String.format(Locale.getDefault(), "Rp. %.2f", monthlyTotal));

                // Update monthly total in Firebase
                updateMonthlyTotalInFirebase(year, month, monthlyTotal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalendarSpendingActivity.this,
                        "Failed to load monthly data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMonthlyTotalInFirebase(int year, int month, double total) {
        String monthYear = String.format(Locale.getDefault(), "%04d-%02d", year, month + 1);
        spendingRef.child("monthly_totals").child(monthYear).setValue(total);
    }

    private void loadDailySpending(String selectedDate) {
        spendingRef.child("completedItems")
                .orderByChild("date")
                .equalTo(selectedDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<ShoppingItem> dailyItems = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);
                            if (item != null) {
                                dailyItems.add(item);
                            }
                        }

                        // Highlight selected date items
                        dailySpendingItems.removeAll(dailyItems);
                        dailySpendingItems.addAll(0, dailyItems);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CalendarSpendingActivity.this,
                                "Failed to load daily spending", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

