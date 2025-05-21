package com.hcmus.management.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;
import com.hcmus.management.adapter.CartAdapter;
import com.hcmus.management.model.FoodItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCartItems;
    private CartAdapter adapter;
    private List<FoodItem> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCartItems = findViewById(R.id.rvCartItems);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        foodItems = new ArrayList<>();
        adapter = new CartAdapter(foodItems);
        rvCartItems.setAdapter(adapter);

        // Thêm item test
        FoodItem item = new FoodItem("Burger With Meat", 12.23, 1, true, R.drawable.sample_burger);
        adapter.addItem(item);
    }
}
