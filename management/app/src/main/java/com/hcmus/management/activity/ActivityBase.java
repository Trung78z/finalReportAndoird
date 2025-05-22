package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hcmus.management.R;

public abstract class ActivityBase extends AppCompatActivity {

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
                if (!(this instanceof ActivityHome)) {
                    startActivity(new Intent(this, ActivityHome.class));
                    overridePendingTransition(0, 0); // Disable transition animation
                }
                return true;
            } else if (itemId == R.id.nav_cart) {
                if (!(this instanceof ActivityOrder)) {
                    startActivity(new Intent(this, ActivityOrder.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            } else if (itemId == R.id.nav_food) {
                if (!(this instanceof ActivityManagerFood)) {
                    startActivity(new Intent(this, ActivityManagerFood.class));
                    overridePendingTransition(0, 0);
                }
                return true;
            } else if (itemId == R.id.nav_profile) {
                if (!(this instanceof ActivityProfile)) {
                    startActivity(new Intent(this, ActivityProfile.class));
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
