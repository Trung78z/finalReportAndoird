package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.management.R;

public class MenuDetailActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        btnAddToCart.setOnClickListener(v -> {
            // Handle add to cart button click
            finish();
        });
    }
}