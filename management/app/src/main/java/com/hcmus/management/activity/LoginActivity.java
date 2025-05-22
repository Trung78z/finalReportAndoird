package com.hcmus.management.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.hcmus.management.R;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_login);
        TextView btnLogin = findViewById(R.id.registerLink);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.signInButton).setOnClickListener(v -> handleLogin());

    }

    private void handleLogin() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.signInButton).setEnabled(false);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AuthRequest.login(this,
                VolleySingleton.getInstance(this).getRequestQueue(),
                email,
                password,
                new AuthRequest.Callback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        progressDialog.dismiss();
                        findViewById(R.id.signInButton).setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                goToHome();
                            } else {
                                // Handle API-reported failure
                                String errorMsg = response.getString("msg");
                                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Error processing response", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN_ERROR", "JSON parsing error", e);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        progressDialog.dismiss();
                        findViewById(R.id.signInButton).setEnabled(true);

                        // Check if this is our API error message
                        if (message.contains("Invalid email or password")) {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other types of errors (network, etc.)
                            Toast.makeText(LoginActivity.this,
                                    "Login failed: Server not responding. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}