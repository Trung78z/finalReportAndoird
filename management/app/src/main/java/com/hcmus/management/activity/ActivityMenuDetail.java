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
        btnAddToCart.setOnClickListener(v ->
                // TODO: handle real add-to-cart logic
                finish()); // TODO: handle real add-to-cart logic
    }
}
