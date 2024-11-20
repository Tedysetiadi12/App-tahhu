package com.tahhu.coba;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class SecurityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        findViewById(R.id.btn_office_security).setOnClickListener(v -> openDurationActivity("Pengamanan Kantor"));
        findViewById(R.id.btn_personal_security).setOnClickListener(v -> openDurationActivity("Pengamanan Pribadi"));
    }

    private void openDurationActivity(String securityType) {
        Intent intent = new Intent(this, DurationActivity.class);
        intent.putExtra("SECURITY_TYPE", securityType);
        startActivity(intent);
    }
}
