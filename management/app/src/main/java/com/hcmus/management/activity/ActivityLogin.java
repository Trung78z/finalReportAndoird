package com.hcmus.management.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class ActivityLogin extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_login);
        TextView btnLogin = findViewById(R.id.registerLink);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, ActivityRegister.class);
            startActivity(intent);
        });
        findViewById(R.id.signupBtn).setOnClickListener(v -> handleLogin());

    }

    private void handleLogin() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.signupBtn).setEnabled(false);
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
                        findViewById(R.id.signupBtn).setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(ActivityLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                goToHome();
                            } else {
                                // Handle API-reported failure
                                String errorMsg = response.getString("msg");
                                Toast.makeText(ActivityLogin.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivityLogin.this, "Error processing response", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN_ERROR", "JSON parsing error", e);
                        }
                    }
                    @Override
                    public void onError(String message) {
                        progressDialog.dismiss();
                        findViewById(R.id.signupBtn).setEnabled(true);

                        // Try to parse server error message if present
                        try {
                            JSONObject errorObj = new JSONObject(message);
                            if (errorObj.has("msg")) {
                                Toast.makeText(ActivityLogin.this, errorObj.getString("msg"), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception ignored) {}


                        // Check if this is our API error message
                        if (message.contains("Invalid email or password")) {
                            Toast.makeText(ActivityLogin.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle other types of errors (network, etc.)
                            Toast.makeText(ActivityLogin.this,
                                    "Login failed: Server not responding. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToHome() {
        Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
        startActivity(intent);
        finish();
    }

}