package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.management.R;

public class ActivityPayment extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            finish();
        });
        
        String total = getIntent().getStringExtra("total");
        // Now you can use 'total', for example:
         TextView tvTotal = findViewById(R.id.tvTotal);
         tvTotal.setText("$ " +total);
    }
}
