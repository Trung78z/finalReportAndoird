package com.hcmus.management.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.hcmus.management.R;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.VolleySingleton;

import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthRequest.initCookieManager(this);
        AuthRequest.restoreRefreshToken(this);
        setContentView(R.layout.activity_splash);
        String refreshToken = AuthRequest.getRefreshToken();
        if (refreshToken != null) {
            requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
            Log.d(TAG, "onCreate called, start refreshTokenIfNeeded");
            refreshTokenIfNeeded();
        } else {
            Log.d(TAG, "No refresh token, going to Login");
            goToLogin();
        }
    }

    private void refreshTokenIfNeeded() {
        AuthRequest.refreshToken(this, requestQueue, new AuthRequest.Callback() {
            @Override
            public void onSuccess(JSONObject response) {
                // Get token from SharedPreferences (already saved by AuthRequest)
                String newAccessToken = AuthRequest.getAccessToken(SplashActivity.this);
                Toast.makeText(SplashActivity.this, "Token refreshed", Toast.LENGTH_SHORT).show();

                if (newAccessToken != null && !newAccessToken.isEmpty()) {
                    goToHome();
                } else {
                    Log.d(TAG, "Token is empty, going to Login");
                    goToLogin();
                }
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "refreshToken error: " + message);
                Toast.makeText(SplashActivity.this, "Token refresh failed", Toast.LENGTH_SHORT).show();
                goToLogin();
            }
        });
    }

    private void goToHome() {
        Log.d(TAG, "Navigating to HomeActivity");
        Toast.makeText(this, "Going to Home", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void goToLogin() {
        Log.d(TAG, "Navigating to LoginActivity");
        Toast.makeText(this, "Going to Login", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
