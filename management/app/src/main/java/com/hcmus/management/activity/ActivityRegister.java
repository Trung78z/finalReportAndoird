package com.hcmus.management.activity;

import android.content.Intent;
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

public class ActivityRegister extends AppCompatActivity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.activity_register);
        TextView signInLink = findViewById(R.id.signInLink);
        Button signupBtn = findViewById(R.id.signupBtn);

        signInLink.setOnClickListener(v -> {
            finish();
        });
        signupBtn.setOnClickListener(v->{
            handleRegister();
        });
    }

    private void handleRegister() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();
        String username = ((EditText) findViewById(R.id.etUsername)).getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.signupBtn).setEnabled(false);


        AuthRequest.register(this,
                VolleySingleton.getInstance(this).getRequestQueue(),
                email,
                password, username,
                new AuthRequest.Callback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        Log.d("REGISTER_RESPONSE", response.toString());
                        try {
                            Log.d("REGISTER_RESPONSE", response.toString());
                            if (response.getBoolean("success")) {
                                Toast.makeText(ActivityRegister.this, "Register successful", Toast.LENGTH_SHORT).show();
                                goToLogin();
                            } else {
                                // Handle API-reported failure
                                String errorMsg = response.getString("msg");
                                Toast.makeText(ActivityRegister.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivityRegister.this, "Error processing response", Toast.LENGTH_SHORT).show();
                            Log.e("LOGIN_ERROR", "JSON parsing error", e);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        findViewById(R.id.signupBtn).setEnabled(true);

                        // Try to parse server error message if present
                        try {
                            JSONObject errorObj = new JSONObject(message);
                            if (errorObj.has("msg")) {
                                Toast.makeText(ActivityRegister.this, errorObj.getString("msg"), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception ignored) {}

                        // Fallback for known error or generic error
                        if (message.contains("Invalid email or password")) {
                            Toast.makeText(ActivityRegister.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityRegister.this,
                                    "Register failed: Server not responding. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToLogin() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }
}