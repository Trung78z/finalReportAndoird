package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmus.management.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                if (!(this instanceof HomeActivity)) {
                    startActivity(new Intent(this, HomeActivity.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            } else if (itemId == R.id.nav_profile) {
//                if (!(this instanceof ProfileActivity)) {
//                    startActivity(new Intent(this, ProfileActivity.class));
//                    overridePendingTransition(0, 0);
//                }
                return true;
            }
            return false;
        });
    }

    private void setActivityLayout(@LayoutRes int layoutResId) {
        FrameLayout container = findViewById(R.id.container);
        LayoutInflater.from(this).inflate(layoutResId, container, true);
    }

    // Bắt buộc các activity con phải cung cấp layout riêng
    protected abstract int getLayoutResourceId();

    // Cho activity con chỉ định item đang active
    protected abstract int getBottomNavigationMenuItemId();

    private void selectCurrentMenuItem() {
        int itemId = getBottomNavigationMenuItemId();
        if (itemId != 0 && bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}