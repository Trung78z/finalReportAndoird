package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmus.management.R;
import com.hcmus.management.model.CardItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.populateCardItems();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_home;
    }


    private void populateCardItems() {
        GridLayout gridContainer = findViewById(R.id.grid_container);
        gridContainer.removeAllViews();

        List<CardItem> foodItems = getCardItems();
        for (CardItem item : foodItems) {
            View cardView = getLayoutInflater().inflate(R.layout.card_fastfood, gridContainer, false);

            ImageView ivFood = cardView.findViewById(R.id.iv_food);
            TextView tvName = cardView.findViewById(R.id.tv_food_name);
            TextView tvPrice = cardView.findViewById(R.id.tv_food_price);

            ivFood.setImageResource(item.getImageResId());
            tvName.setText(item.getName());
            tvPrice.setText(String.format("$%.2f", item.getPrice()));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(this, MenuDetailActivity.class);
                startActivity(intent);
//                openFoodDetails(item);
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            gridContainer.addView(cardView, params);
        }
    }


    private List<CardItem> getCardItems() {
        List<CardItem> items = new ArrayList<>();
        items.add(new CardItem("12331", "Burger With Meat", 12.23, R.drawable.burger_with_meat));
        items.add(new CardItem("12331", "Burger With Meat", 12.23, R.drawable.burger_with_meat));
        items.add(new CardItem("12331", "Burger With Meat", 12.23, R.drawable.burger_with_meat));
        items.add(new CardItem("1233", "Chicken Sandwic", 14, R.drawable.burger_with_meat));
        items.add(new CardItem("1234", "French Fries", 17, R.drawable.burger_with_meat));
        items.add(new CardItem("1235", "Pizza", 12.23, R.drawable.burger_with_meat));
        items.add(new CardItem("1236", "Burger With Meat", 18, R.drawable.burger_with_meat));
        return items;
    }
}
