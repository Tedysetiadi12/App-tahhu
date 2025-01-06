package com.tahhu.id;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SecurityListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SecurityAdapter securityAdapter;
    private List<SecurityProfile> securityProfiles;
    private String securityType;
    private String period;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_list);

        securityType = getIntent().getStringExtra("securityType");
        period = getIntent().getStringExtra("period");
        price = getIntent().getStringExtra("price");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        securityProfiles = new ArrayList<>();
        // Add sample data (replace with actual data from your database)
        securityProfiles.add(new SecurityProfile("John Doe", "5 years experience", "Combat trained", R.drawable.ic_user1));
        securityProfiles.add(new SecurityProfile("Jane Smith", "3 years experience", "Surveillance expert", R.drawable.ic_user2));


        securityAdapter = new SecurityAdapter(this, securityProfiles, securityType, period, price);
        recyclerView.setAdapter(securityAdapter);

    }
}

