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
import com.hcmus.management.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {
    private RecyclerView rvCartItems;

    private CartAdapter adapter;
    private List<CartItem> cartItems;
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

        cartItems = new ArrayList<>();
        adapter = new CartAdapter(cartItems);
        rvCartItems.setAdapter(adapter);


        CartItem item = new CartItem("Burger With Meat", 12.23, 1, true, R.drawable.sample_burger);
        adapter.addItem(item);

        btnOrderNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, PaymentActivity.class);
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