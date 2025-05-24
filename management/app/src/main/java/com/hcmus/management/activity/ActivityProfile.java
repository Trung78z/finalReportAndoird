package com.hcmus.management.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hcmus.management.R;
import com.hcmus.management.dto.UserUpdateDTO;
import com.hcmus.management.network.AuthRequest;
import com.hcmus.management.network.Callback;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityProfile extends ActivityBase {
    private EditText etFullName, etCity, etPhone, etEmail, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        etPhone = findViewById(R.id.etPhone);
        etFullName = findViewById(R.id.etFullName);
        etCity = findViewById(R.id.etCity);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        getUserProfile();
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            saveProfile();
        });
    }

    private void saveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullName.isEmpty() || city.isEmpty() || address.isEmpty()
                || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setFullName(fullName);
        userUpdateDTO.setCity(city);
        userUpdateDTO.setAddress(address);
        userUpdateDTO.setEmail(email);
        userUpdateDTO.setPhoneNumber(phone);

        AuthRequest.updateUser(this, userUpdateDTO, new Callback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(ActivityProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object message) {
                String errorMsg = "Update failed!";
                if (message instanceof String) {
                    errorMsg = (String) message;
                } else if (message instanceof VolleyError) {
                    errorMsg = ((VolleyError) message).getMessage();
                }
                Toast.makeText(ActivityProfile.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserProfile() {
        AuthRequest.getProfile(this, new Callback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject msg = response.getJSONObject("msg");
                    etFullName.setText(msg.getString("fullName"));
                    etPhone.setText(msg.getString("phoneNumber"));
                    etAddress.setText(msg.getString("address"));
                    etCity.setText(msg.getString("city"));
                    etEmail.setText(msg.getString("email"));
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(Object message) {
                String errorMsg = "Get failed!";
                if (message instanceof String) {
                    errorMsg = (String) message;
                } else if (message instanceof VolleyError) {
                    errorMsg = ((VolleyError) message).getMessage();
                }
                Toast.makeText(ActivityProfile.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
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
