package com.tahhu.id;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class UsageStatsFragment extends Fragment {
    private BarChart usageChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage_stats, container, false);
        usageChart = view.findViewById(R.id.usage_chart);

        setupUsageChart();

        return view;
    }

    private void setupUsageChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));
        entries.add(new BarEntry(6f, 20f));

        BarDataSet dataSet = new BarDataSet(entries, "Daily Usage (GB)");
        BarData barData = new BarData(dataSet);
        usageChart.setData(barData);
        usageChart.invalidate();
    }
}
