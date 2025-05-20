package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private EditText etPromoCode;
    private Button btnApplyPromo, btnOrderNow;
    private TextView tvTotalItems, tvDiscount, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        rvCartItems = findViewById(R.id.rvCartItems);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btnOrderNow = findViewById(R.id.btnOrderNow);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);

        // TODO: Set up RecyclerView adapter, promo code logic, and order button
    }
}