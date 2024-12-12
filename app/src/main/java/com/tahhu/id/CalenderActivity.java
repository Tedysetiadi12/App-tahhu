package com.tahhu.id;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CalenderActivity extends AppCompatActivity {

    private static List<ShoppingItem> monthlySpending = new ArrayList<>();
    private LineChart lineChart;
    private MaterialCalendarView materialCalendarView;
    private RecyclerView recyclerView;
    private SpendingAdapter spendingAdapter;
    private TextView tvTotalSpending;

    public static void addSpending(ShoppingItem item) {
        monthlySpending.add(item);
    }

    // Daftar libur nasional
    private static final Set<String> nationalHolidays = new HashSet<>(Arrays.asList(
            "2024-01-01", "2024-04-14", "2024-05-01", "2024-08-17", "2024-12-25"
    ));


    // Deskripsi libur
    private static final Map<String, String> holidayDescriptions = new HashMap<>();

    static {
        holidayDescriptions.put("2024-01-01", "Tahun Baru Masehi");
        holidayDescriptions.put("2024-04-14", "Hari Raya Paskah");
        holidayDescriptions.put("2024-05-01", "Hari Buruh Internasional");
        holidayDescriptions.put("2024-08-17", "Hari Kemerdekaan Indonesia");
        holidayDescriptions.put("2024-12-25", "Hari Raya Natal");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        lineChart = findViewById(R.id.lineChart);
        materialCalendarView = findViewById(R.id.materialCalendarView);
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalSpending = findViewById(R.id.tvTotalSpending);

        setupRecyclerView();
        setupCalendar();

        // Tampilkan grafik untuk bulan dan tahun saat ini
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        showMonthlySpendingGraph(currentYear, currentMonth);

        // Firebase initialization
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference spendingRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("shopping_list").child("spending");


        // Set listener to update tvTotalSpending
        spendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double totalSpending = snapshot.getValue(Double.class);
                if (totalSpending != null) {
                    String formattedSpending = String.format(Locale.getDefault(), "Rp. %.2f", totalSpending);
                    tvTotalSpending.setText(formattedSpending);
                } else {
                    tvTotalSpending.setText("Rp. 0.00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalenderActivity.this, "Gagal memuat data spending", Toast.LENGTH_SHORT).show();
            }
        });
        // Tambahkan listener untuk deteksi perubahan bulan
        materialCalendarView.setOnMonthChangedListener((widget, date) -> {
            int selectedMonth = date.getMonth(); // Bulan yang dipilih (0-indexed)
            int selectedYear = date.getYear();
            showMonthlySpendingGraph(selectedYear, selectedMonth);
        });

        Button btnSelectCategory = findViewById(R.id.btnSelectCategory);
        // Setel default ke "Hari Ini"
        btnSelectCategory.setText("Hari Ini");
        btnSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Daftar pilihan kategori
                final String[] categories = {"Hari Ini", "7 Hari", "30 Hari"};

                // Membuat dialog untuk memilih kategori
                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                builder.setTitle("Pilih Pengeluaran")
                        .setItems(categories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Ketika kategori dipilih
                                String selectedCategory = categories[which];
                                btnSelectCategory.setText(selectedCategory);  // Update text pada button
                            }
                        })
                        .create()
                        .show();
            }
        });


    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spendingAdapter = new SpendingAdapter();
        recyclerView.setAdapter(spendingAdapter);

        ArrayList<SpendingItem> items = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (String holiday : nationalHolidays) {
            String[] parts = holiday.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // Bulan dimulai dari 0
            int day = Integer.parseInt(parts[2]);

            calendar.set(year, month, day);

            // Hari dalam seminggu
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());

            // Deskripsi libur
            String description = holidayDescriptions.getOrDefault(holiday, "Libur Nasional");

            // Tambahkan item
            items.add(new SpendingItem(description, dayOfWeek, String.valueOf(day)));
        }

        spendingAdapter.setData(items);
    }
    private void setupCalendar() {
        // Highlight national holidays
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                // Format the date as "yyyy-MM-dd"
                String date = String.format("%04d-%02d-%02d", day.getYear(), day.getMonth() + 1, day.getDay());
                return nationalHolidays.contains(date); // Check if the date is a national holiday
            }

            @Override
            public void decorate(DayViewFacade view) {
                // Set a different background color for national holidays (e.g., red)
                view.setBackgroundDrawable(ContextCompat.getDrawable(CalenderActivity.this, R.drawable.holiday_background));
                view.addSpan(new ForegroundColorSpan(Color.RED)); // Change text color to white
            }
        });
        // Highlight Sundays in red
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.getCalendar().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new ForegroundColorSpan(Color.RED));
            }
        });
// Example: Add a decorator for specific days
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                // Add your condition here
                return true;
            }

            @Override
            public void decorate(DayViewFacade view) {
                // Customize day view here
                view.setBackgroundDrawable(ContextCompat.getDrawable(CalenderActivity.this, R.drawable.custom_day_background));
            }
        });



        // Highlight today with a background
        Drawable todayBackground = ContextCompat.getDrawable(this, R.drawable.today_background);
        materialCalendarView.addDecorator(new TodayDecorator(todayBackground));

        // Add date selection behavior
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(CalenderActivity.this, "Selected date: " + date.getDate(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMonthlySpendingGraph(int year, int month) {
        Map<Integer, Double> dailySpending = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        double totalSpending = 0;

        // Mengisi data pengeluaran harian
        for (ShoppingItem item : monthlySpending) {
            calendar.setTime(item.getCompletionDate());
            int itemMonth = calendar.get(Calendar.MONTH);
            int itemYear = calendar.get(Calendar.YEAR);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            if (itemMonth == month && itemYear == year) {
                double spending = item.getPrice() * item.getQuantity();
                dailySpending.put(dayOfMonth, dailySpending.getOrDefault(dayOfMonth, 0.0) + spending);
                totalSpending += spending;
            }
        }
        tvTotalSpending.setText("Rp " + String.valueOf(totalSpending));

        ArrayList<Entry> entries = new ArrayList<>();

        // Tambahkan titik data untuk setiap hari di bulan (default 0 jika tidak ada data)
        for (int day = 1; day <= daysInMonth; day++) {
            double spending = dailySpending.getOrDefault(day, 0.0);
            entries.add(new Entry(day, (float) spending));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Pengeluaran Harian");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setLineWidth(2f); // Menambah ketebalan garis untuk kenyamanan
        dataSet.setCircleRadius(4f); // Menambah ukuran lingkaran pada titik data
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("Grafik Pengeluaran Bulanan");
        lineChart.setDescription(description);

        // Menambahkan Tanggal pada Sumbu X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Menampilkan di bawah
        xAxis.setGranularity(1f); // Menyaring sumbu x untuk tanggal
        xAxis.setLabelCount(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) / 2); // Show 1 label for every 3rd day
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int day = (int) value;
                // Show every 3rd day of the month
                if (day % 3 == 0) {
                    return String.format("%02d", day); // Format as two-digit day
                }
                return ""; // No label for other days
            }
        });

        // Menyesuaikan Sumbu Y
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false); // Menyembunyikan garis grid
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Menyembunyikan sumbu kanan
        // Konfigurasi sumbu Y
        yAxisLeft.setAxisMinimum(0f); // Pastikan mulai dari 0
        lineChart.getAxisRight().setEnabled(false);
        // Menambah tampilan grid garis untuk kenyamanan
        lineChart.getAxisLeft().setDrawGridLines(true);

        lineChart.getXAxis().setDrawGridLines(false); // Menyembunyikan garis grid pada sumbu X

        // Menyembunyikan Legend
        lineChart.getLegend().setEnabled(false);

        // Memperbarui chart
        lineChart.invalidate();
    }
}
