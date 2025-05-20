package com.hcmus.management.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.management.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
    }

    @Override
    protected int getLayoutResourceId() {
        Log.d("HomeActivity", "Inflating layout: activity_home");
        return R.layout.activity_home;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_home;
    }
}
