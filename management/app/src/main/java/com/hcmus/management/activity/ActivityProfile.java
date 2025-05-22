package com.hcmus.management.activity;

import android.os.Bundle;

import com.hcmus.management.R;

public class ActivityProfile extends ActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_user_profile_update;
    }

    @Override
    protected int getBottomNavigationMenuItemId() {
        return R.id.nav_profile;
    }

}
