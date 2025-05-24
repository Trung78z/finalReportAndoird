package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;
import com.hcmus.management.adapter.AdapterFood;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.FoodRequest;

import java.util.ArrayList;
import java.util.List;

public class ActivityManagerFood extends ActivityBase {
    private AdapterFood adapter;
    private List<FoodItem> foodItems;
    private ImageButton btnBack;
    private RecyclerView rvCartItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        foodItems = new ArrayList<>();
        adapter = new AdapterFood(foodItems);
        rvCartItems.setAdapter(adapter);

        fetchFoodList(); // <-- Fetch from API

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        Button btnAddNew = findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityCreateFood.class);
            startActivity(intent);
        });
    }

    private void fetchFoodList() {
        FoodRequest.fetchFoodList(this, new FoodRequest.FoodListCallback() {
            @Override
            public void onSuccess(List<FoodItem> items) {
                foodItems.clear();
                foodItems.addAll(items);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String message) {
                // Handle error (show Toast, etc.)
            }
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
