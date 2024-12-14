package com.tahhu.id;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import com.tahhu.id.EventDecorator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarSpendingActivity extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private TextView tvMonthlyTotal;
    private RecyclerView rvDailySpending;
    private DatabaseReference spendingRef;
    private String userId;
    private ImageView btnBack;
    private DailySpendingAdapter adapter;
    private ArrayList<ShoppingItem> dailySpendingItems = new ArrayList<>();
    private Map<CalendarDay, String> nationalHolidays = new HashMap<>();
    private RequestQueue requestQueue;
    private Calendar currentDisplayedMonth = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_spending);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        spendingRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("shopping_list");

        requestQueue = Volley.newRequestQueue(this);

        initializeViews();
        setupCalendar();
        setupRecyclerView();
        loadNationalHolidays();
        loadMonthlySpendingAndItems(currentDisplayedMonth.get(Calendar.YEAR), currentDisplayedMonth.get(Calendar.MONTH));

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void updateMonthlyTotalInFirebase(int year, int month, double total) {
        String monthYear = String.format(Locale.getDefault(), "%04d-%02d", year, month + 1);
        spendingRef.child("monthly_totals").child(monthYear).setValue(total);
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal);
        rvDailySpending = findViewById(R.id.rvDailySpending);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            CalendarDay selectedDay = CalendarDay.from(date.getYear(), date.getMonth(), date.getDay());
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(date.getYear(), date.getMonth(), date.getDay());

            if (currentDisplayedMonth.get(Calendar.MONTH) != date.getMonth() ||
                    currentDisplayedMonth.get(Calendar.YEAR) != date.getYear()) {
                currentDisplayedMonth.set(Calendar.YEAR, date.getYear());
                currentDisplayedMonth.set(Calendar.MONTH, date.getMonth());
                loadMonthlySpendingAndItems(date.getYear(), date.getMonth());
            }

            if (nationalHolidays.containsKey(selectedDay)) {
                showHolidayDialog(nationalHolidays.get(selectedDay));
            } else {
                loadDailySpending(dateFormat.format(selectedDate.getTime()));
            }
        });

        calendarView.setOnMonthChangedListener((widget, date) -> {
            currentDisplayedMonth.set(Calendar.YEAR, date.getYear());
            currentDisplayedMonth.set(Calendar.MONTH, date.getMonth());
            loadMonthlySpendingAndItems(date.getYear(), date.getMonth());
        });
    }


    private void setupRecyclerView() {
        adapter = new DailySpendingAdapter(dailySpendingItems);
        rvDailySpending.setLayoutManager(new LinearLayoutManager(this));
        rvDailySpending.setAdapter(adapter);
    }

    private void loadNationalHolidays() {
        String apiKey = "ZFbWS739qLMt2P9u8yyMQWIbIP6WYj64"; // Replace with your API key
        String country = "ID"; // Country code for Indonesia
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String url = "https://calendarific.com/api/v2/holidays?&api_key=" + apiKey + "&country=" + country + "&year=" + year;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject responseData = response.getJSONObject("response");
                            JSONArray holidays = responseData.getJSONArray("holidays");

                            for (int i = 0; i < holidays.length(); i++) {
                                JSONObject holiday = holidays.getJSONObject(i);
                                String name = holiday.getString("name");
                                String dateStr = holiday.getJSONObject("date").getString("iso");

                                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date date = isoFormat.parse(dateStr);
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);

                                CalendarDay holidayDate = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                nationalHolidays.put(holidayDate, name);
                            }

                            // Add decorators for holidays
                            calendarView.addDecorator(new EventDecorator(CalendarSpendingActivity.this, new ArrayList<>(nationalHolidays.keySet()), R.color.active_button_color));
                        } catch (JSONException | java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CalendarSpendingActivity.this, "Failed to load holidays", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void showHolidayDialog(String holidayDescription) {
        new AlertDialog.Builder(this)
                .setTitle("Hari Libur Nasional")
                .setMessage(holidayDescription)
                .setPositiveButton("OK", null)
                .show();
    }

    private String formatDate(CalendarDay day) {
        return String.format(Locale.getDefault(), "%02d %02d %04d", day.getDay(), day.getMonth() + 1, day.getYear());
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

    private void loadDailySpending(String selectedDate) {
        spendingRef.child("completedItems")
                .orderByChild("date")
                .equalTo(selectedDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dailySpendingItems.clear();

                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);
                            if (item != null) {
                                dailySpendingItems.add(item);
                            }
                        }

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
