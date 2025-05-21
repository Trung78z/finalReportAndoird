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

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Handle navigation item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                if (!(this instanceof HomeActivity)) {
                    startActivity(new Intent(this, HomeActivity.class));
                    overridePendingTransition(0, 0); // Disable transition animation
                }
                return true;
            } else if (itemId == R.id.nav_cart) {
                if (!(this instanceof OrderActivity)) {
                    startActivity(new Intent(this, OrderActivity.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            } else if (itemId == R.id.nav_food) {
                if (!(this instanceof ManagerFoodActivity)) {
                    startActivity(new Intent(this, ManagerFoodActivity.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            } else if (itemId == R.id.nav_profile) {
                if (!(this instanceof ProfileActivity)) {
                    startActivity(new Intent(this, ProfileActivity.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            }


            return false;
        });

        // Inflate child activity layout into the container
        setActivityLayout(getLayoutResourceId());

        // Mark the current navigation item as selected
        selectCurrentMenuItem();
    }

    /**
     * Inflate the child activity layout inside the container of the base layout.
     */
    private void setActivityLayout(@LayoutRes int layoutResId) {
        FrameLayout container = findViewById(R.id.container);
        LayoutInflater.from(this).inflate(layoutResId, container, true);
    }

    /**
     * Subclasses must provide their own layout resource ID.
     */
    protected abstract int getLayoutResourceId();

    /**
     * Subclasses must specify the currently selected navigation item.
     */
    protected abstract int getBottomNavigationMenuItemId();

    /**
     * Highlight the current bottom navigation item based on the activity.
     */
    private void selectCurrentMenuItem() {
        int itemId = getBottomNavigationMenuItemId();
        if (itemId != 0 && bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}
