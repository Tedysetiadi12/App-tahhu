package com.tahhu.coba;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CalenderActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;
    private BarChart barChart;
    private RecyclerView recyclerView;
    private SpendingAdapter spendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        materialCalendarView = findViewById(R.id.materialCalendarView);
        barChart = findViewById(R.id.barChart);
        recyclerView = findViewById(R.id.recyclerView);

        // Setup RecyclerView for daily spending
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spendingAdapter = new SpendingAdapter();
        recyclerView.setAdapter(spendingAdapter);

        // Setup Calendar View with Month Selector
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                // Tindak lanjut ketika bulan berubah
                showDailySpending(date);
                showSpendingGraph(date);
            }
        });

        // Example data loading (dummy data or from DB)
        loadSpendingData();
    }

    private void showDailySpending(CalendarDay date) {
        // You would query your database for daily spending
        // Here we are just showing a message as an example
        Toast.makeText(this, "Displaying spending for: " + date.getDate(), Toast.LENGTH_SHORT).show();
    }

    private void showSpendingGraph(CalendarDay date) {
        // Membuat data dummy acak untuk grafik
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Membuat data acak (misalnya untuk 7 hari)
        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i + 1f, (float) (Math.random() * 500)));  // Acak nilai pengeluaran
        }

        BarDataSet dataSet = new BarDataSet(entries, "Pengeluaran Mingguan");
        BarData barData = new BarData(dataSet);

        // Menampilkan data ke dalam chart
        barChart.setData(barData);

        // Mengatur warna batang grafik
        dataSet.setColor(Color.BLUE);  // Mengubah warna batang menjadi biru

        // Menyembunyikan grid jika tidak diinginkan
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        // Mengaktifkan deskripsi grafik
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText("Grafik Pengeluaran");

        // Menyembunyikan legend jika tidak diperlukan
        barChart.getLegend().setEnabled(false);

        // Memperbarui chart
        barChart.invalidate(); // Memperbarui chart
    }

    // Function to generate random data (you could replace this with actual user input)
    private ArrayList<Float> generateRandomData() {
        ArrayList<Float> randomData = new ArrayList<>();

        // Generate random data for demonstration (e.g., 7 days of spending)
        for (int i = 0; i < 7; i++) {
            randomData.add((float) (Math.random() * 1000));  // Random value between 0 and 1000
        }

        return randomData;
    }

    private void loadSpendingData() {
        // Example of loading spending data into RecyclerView (this should come from a database)
        ArrayList<SpendingItem> items = new ArrayList<>();
        items.add(new SpendingItem("Coffee", 10));
        items.add(new SpendingItem("Lunch", 20));
        items.add(new SpendingItem("Dinner", 30));

        spendingAdapter.setData(items);
    }
}
