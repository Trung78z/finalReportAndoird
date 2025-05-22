package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;
import com.hcmus.management.adapter.CartAdapter;
import com.hcmus.management.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class ActivityOrder extends ActivityBase {
    private RecyclerView rvCartItems;

    private CartAdapter adapter;
    private List<FoodItem> foodItems;
    private EditText etPromoCode;
    private Button btnApplyPromo, btnOrderNow;
    private TextView tvTotalItems, tvDiscount, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rvCartItems = findViewById(R.id.rvCartItems);
        etPromoCode = findViewById(R.id.etPromoCode);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btnOrderNow = findViewById(R.id.btnAddNew);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        foodItems = new ArrayList<>();
        adapter = new CartAdapter(foodItems);
        rvCartItems.setAdapter(adapter);


        btnOrderNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityPayment.class);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_cart;
    }
}