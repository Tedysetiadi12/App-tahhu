package com.tahhu.id;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.widget.TextView;

public class SecurityTypeActivity extends AppCompatActivity {
    private Button btnDaily, btnMonthly, btnYearly;
    private TextView tvDailyPrice, tvMonthlyPrice, tvYearlyPrice;
    private String securityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_type);

        securityType = getIntent().getStringExtra("securityType");

        btnDaily = findViewById(R.id.btn_daily);
        btnMonthly = findViewById(R.id.btn_monthly);
        btnYearly = findViewById(R.id.btn_yearly);
        tvDailyPrice = findViewById(R.id.tv_daily_price);
        tvMonthlyPrice = findViewById(R.id.tv_monthly_price);
        tvYearlyPrice = findViewById(R.id.tv_yearly_price);

        // Set prices based on security type
        if ("office".equals(securityType)) {
            tvDailyPrice.setText("Rp 500.000 / hari");
            tvMonthlyPrice.setText("Rp 12.000.000 / bulan");
            tvYearlyPrice.setText("Rp 120.000.000 / tahun");
        } else {
            tvDailyPrice.setText("Rp 300.000 / hari");
            tvMonthlyPrice.setText("Rp 8.000.000 / bulan");
            tvYearlyPrice.setText("Rp 80.000.000 / tahun");
        }

        View.OnClickListener periodClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String period = "";
                String price = "";
                if (v.getId() == R.id.btn_daily) {
                    period = "Harian";
                    price = tvDailyPrice.getText().toString();
                } else if (v.getId() == R.id.btn_monthly) {
                    period = "Bulanan";
                    price = tvMonthlyPrice.getText().toString();
                } else if (v.getId() == R.id.btn_yearly) {
                    period = "Tahunan";
                    price = tvYearlyPrice.getText().toString();
                }

                Intent intent = new Intent(SecurityTypeActivity.this, SecurityListActivity.class);
                intent.putExtra("securityType", securityType);
                intent.putExtra("period", period);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        };

        btnDaily.setOnClickListener(periodClickListener);
        btnMonthly.setOnClickListener(periodClickListener);
        btnYearly.setOnClickListener(periodClickListener);
    }
}

