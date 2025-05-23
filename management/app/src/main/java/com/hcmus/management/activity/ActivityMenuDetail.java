package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hcmus.management.R;
import com.hcmus.management.model.FoodItem;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.CartRequest;
import com.hcmus.management.network.VolleySingleton;

public class ActivityMenuDetail extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnAddToCart;
    private ImageView imgFood;

    private TextView tvFoodName;
    private TextView tvFoodPrice;
    private TextView tvQuantity;
    private TextView tvDescription;
    private TextView tvTotalPrice;

    private Button btnMinus;
    private Button btnPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        // 🔁 Khởi tạo View sau setContentView
        imgFood = findViewById(R.id.img_food);
        tvFoodName = findViewById(R.id.tv_food_name);
        tvFoodPrice = findViewById(R.id.tv_food_price);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvDescription = findViewById(R.id.tv_description);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ intent
        FoodItem foodItem = (FoodItem) getIntent().getSerializableExtra("food_item");

        if (foodItem != null) {
            Glide.with(this)
                    .load(foodItem.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(imgFood);

            tvFoodName.setText(foodItem.getName());
            tvFoodPrice.setText(String.format("$%.2f", foodItem.getPrice()));
            tvDescription.setText(foodItem.getDescription());
            tvTotalPrice.setText(String.format("$%.2f", foodItem.getPrice()));
        }
        btnMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(tvQuantity.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                tvQuantity.setText(String.valueOf(currentQuantity));
                tvTotalPrice.setText(String.format("$%.2f", foodItem.getPrice() * currentQuantity));
            }
        });
        btnPlus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(tvQuantity.getText().toString());
            currentQuantity++;
            tvQuantity.setText(String.valueOf(currentQuantity));
            tvTotalPrice.setText(String.format("$%.2f", foodItem.getPrice() * currentQuantity));
        });

        btnBack.setOnClickListener(v -> finish());
        btnAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (foodItem != null) {
                CartRequest.order(
                        ActivityMenuDetail.this,
                        VolleySingleton.getInstance(ActivityMenuDetail.this).getRequestQueue(),
                        foodItem.getId(), // assuming getId() returns foodId as String
                        quantity,
                        new AuthRequest.Callback() {
                            @Override
                            public void onSuccess(org.json.JSONObject response) {
                                android.widget.Toast.makeText(ActivityMenuDetail.this, "Added to cart!", android.widget.Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            @Override
                            public void onError(String message) {
                                android.widget.Toast.makeText(ActivityMenuDetail.this, "Add to cart failed: " + message, android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }
}
