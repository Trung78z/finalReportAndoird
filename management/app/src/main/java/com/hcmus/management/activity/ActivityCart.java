package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.management.R;
import com.hcmus.management.adapter.AdapterCart;
import com.hcmus.management.model.CartItem;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.CartRequest;

import java.util.ArrayList;
import java.util.List;

public class ActivityCart extends ActivityBase {
    private RecyclerView rvCartItems;

    private AdapterCart adapter;
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
        adapter = new AdapterCart(foodItems);
        rvCartItems.setAdapter(adapter);

        adapter.setOnCartChangeListener((totalQuantity, total, discount, totalAfterDiscount) -> {
            tvTotalItems.setText(String.valueOf(totalQuantity));
            tvTotal.setText(String.format("%.2f", totalAfterDiscount));
            tvDiscount.setText(String.format("-%.2f", discount));
        });


        CartRequest.fetchCartList(this, new CartRequest.CartListCallback() {
            @Override
            public void onSuccess(List<CartItem> cartItems) {
                foodItems.clear();
                double total = 0;
                int totalQuantity = 0;
                for (CartItem cartItem : cartItems) {
                    FoodItem food = cartItem.getFood();
                    food.setQuantity(cartItem.getQuantity());
                    foodItems.add(food);
                    food.setCartId(cartItem.getId());
                    total += food.getPrice() * food.getQuantity();
                    totalQuantity += cartItem.getQuantity();
                }
                double discount = total * 0.05;
                double totalAfterDiscount = total - discount;

                adapter.notifyDataSetChanged();
                tvTotal.setText(String.format("%.2f", totalAfterDiscount));
                tvTotalItems.setText(String.valueOf(totalQuantity));
                tvDiscount.setText(String.format("-%.2f", discount));
            }

            @Override
            public void onError(String message) {
            }
        });

        btnOrderNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityPayment.class);
            intent.putExtra("total", tvTotal.getText());
            intent.putExtra("totalItem", tvTotalItems.getText());
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_cart;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_cart;
    }
}