package com.hcmus.management.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmus.management.common.Api;

import org.json.JSONException;
import org.json.JSONObject;

public class CartRequest {
    public static void order(Context context, RequestQueue requestQueue,
                             String foodId, Integer quantity, AuthRequest.Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("foodId", foodId);
            requestBody.put("quantity", quantity);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Api.createCart,
                    requestBody,
                    response -> onSuccess(context, response, callback),
                    error -> onError(error, callback)
            ) {
                @Override
                public java.util.Map<String, String> getHeaders() {
                    java.util.Map<String, String> headers = new java.util.HashMap<>();
                    String accessToken = AuthRequest.getAccessToken(context);
                    if (accessToken != null) {
                        headers.put("Authorization", "Bearer " + accessToken);
                    }
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);
        } catch (JSONException e) {
            callback.onError("Invalid login request format");
        }
    }

    private static void onSuccess(Context context, JSONObject response, AuthRequest.Callback callback) {
        try {
            if (response.getBoolean("success")) {
                callback.onSuccess(response);
            } else {
                callback.onError(response.getString("msg"));
            }
        } catch (JSONException e) {
            callback.onError("Invalid login response format");
        }
    }

    private static void onError(VolleyError error, AuthRequest.Callback callback) {
        String errorMsg = "Login failed";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String jsonString = new String(error.networkResponse.data,
                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                // Pass the full JSON string to the callback for better error handling in UI
                callback.onError(jsonString);
                return;
            } catch (Exception e) {
                errorMsg = "Invalid credentials";
            }
        }
        callback.onError(errorMsg);
    }

}
