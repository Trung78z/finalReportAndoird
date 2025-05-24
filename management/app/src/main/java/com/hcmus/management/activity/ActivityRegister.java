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
import com.android.volley.VolleyError;
import com.hcmus.management.R;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.Callback;
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
        signupBtn.setOnClickListener(v -> {
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
                new Callback() {
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
                    public void onError(Object message) {
                        findViewById(R.id.signupBtn).setEnabled(true);

                        String errorMessage = "Register failed: Server not responding. Please try again later.";

                        if (message instanceof VolleyError) {
                            VolleyError error = (VolleyError) message;

                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                try {
                                    String body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject errorObj = new JSONObject(body);

                                    if (errorObj.has("msg")) {
                                        errorMessage = errorObj.getString("msg");
                                    }
                                } catch (Exception e) {
                                    Log.e("REGISTER_ERROR", "Failed to parse error body", e);
                                }
                            }

                        } else if (message instanceof String) {
                            String msgStr = (String) message;
                            if (msgStr.contains("Invalid email or password")) {
                                errorMessage = msgStr;
                            }
                        }

                        Toast.makeText(ActivityRegister.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void goToLogin() {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }
}