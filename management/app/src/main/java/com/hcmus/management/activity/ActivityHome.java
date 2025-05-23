package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcmus.management.R;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.FoodRequest;

import java.util.ArrayList;
import java.util.List;

public class ActivityHome extends ActivityBase {
    private List<FoodItem> foodItems = new ArrayList<>();
    private List<FoodItem> originFoodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchFoodList();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_home;
    }


    private void populateCardItems(List<FoodItem> foodItems) {
        GridLayout gridContainer = findViewById(R.id.grid_container);
        gridContainer.removeAllViews();
        for (FoodItem item : foodItems) {
            View cardView = getLayoutInflater().inflate(R.layout.card_fastfood, gridContainer, false);

            ImageView ivFood = cardView.findViewById(R.id.iv_food);
            TextView tvName = cardView.findViewById(R.id.tv_food_name);
            TextView tvPrice = cardView.findViewById(R.id.tv_food_price);

            Glide.with(ivFood.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(ivFood);
            tvName.setText(item.getName());
            tvPrice.setText(String.format("$%.2f", item.getPrice()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ActivityMenuDetail.class);
                // Pass the food item to the detail activity
                intent.putExtra("food_item", item);
                startActivity(intent);
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            gridContainer.addView(cardView, params);
            findViewById(R.id.burger).setOnClickListener(v -> {
                searchFoodItems("burger");
            });
            findViewById(R.id.taco).setOnClickListener(v -> {
                searchFoodItems("toco");
            });

        }
    }

    private void searchFoodItems(String query) {

        if (query.equals("burger"))
        {
            for (FoodItem item : originFoodItems) {
                if (item.getCategoryId() == 1) {
                    foodItems.add(item);
                }
            }
        }
      else {
            for (FoodItem item : originFoodItems) {
                if (item.getCategoryId() == 2) {
                    foodItems.add(item);
                }
            }
        }
    }

    private void fetchFoodList() {
        FoodRequest.fetchFoodList(this, new FoodRequest.FoodListCallback() {
            @Override
            public void onSuccess(List<FoodItem> items) {
                originFoodItems.clear();
                originFoodItems.addAll(items);
                foodItems.clear();
                // find categoryId = 1
                for (FoodItem item : items) {
                    if (item.getCategoryId() == 1) {
                        foodItems.add(item);
                    }
                }
                populateCardItems(foodItems); // <-- update UI after data is loaded
            }

            @Override
            public void onError(String message) {
                // Handle error (show Toast, etc.)
            }
        });
    }
}
