package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;
import com.hcmus.management.adapter.CartAdapter;
import com.hcmus.management.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class ManagerFoodActivity extends BaseActivity {
    private CartAdapter adapter;
    private List<FoodItem> foodItems;
    private ImageButton btnBack;
    private RecyclerView rvCartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        foodItems = new ArrayList<>();
        adapter = new CartAdapter(foodItems);
        rvCartItems.setAdapter(adapter);
        FoodItem item = new FoodItem("Burger With Meat", 12.23, 1, true, R.drawable.sample_burger);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);
        adapter.addItem(item);



        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        Button btnAddNew = findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateFastFoodActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_manager_fastfood;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_food;
    }
}
