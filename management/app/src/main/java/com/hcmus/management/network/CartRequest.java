package com.hcmus.management.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmus.management.common.Api;
import com.hcmus.management.model.CartItem;
import com.hcmus.management.model.Category;
import com.hcmus.management.model.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public interface CartListCallback {
        void onSuccess(List<CartItem> cartItems);

        void onError(String message);
    }

    public static void fetchCartList(Context context, CartListCallback callback) {
        RequestQueue requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Api.getCart,
                null,
                response -> {
                    List<CartItem> cartItems = new ArrayList<>();
                    try {
                        Log.d("CartRequest", "Response: " + response.toString());
                        JSONArray arr = response.getJSONArray("msg");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            JSONObject foodObj = obj.getJSONObject("food");
                            String id = foodObj.optString("id", "");
                            String name = foodObj.optString("name", "");
                            String description = foodObj.optString("description", "");
                            double price = foodObj.optDouble("price", 0);
                            String imageUrl = foodObj.optString("imageUrl", "");
                            JSONObject catObj = foodObj.getJSONObject("category");
                            int categoryId = catObj.optInt("id", 0);
                            String categoryName = catObj.optString("name", "");
                            Category category = new Category(categoryId, categoryName);

                            int quantity = obj.optInt("quantity", 1);

                            FoodItem food = new FoodItem(id, name, description, price, quantity, Api.baseUrl + imageUrl, categoryId);
                            cartItems.add(new CartItem(id, quantity, food));
                        }
                        callback.onSuccess(cartItems);
                    } catch (JSONException e) {
                        Log.e("CartRequest", "Parse error: " + e.getMessage(), e);
                        callback.onError("Parse error");
                    }
                },
                error -> {
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " (code: " + error.networkResponse.statusCode + ")";
                    }
                    Log.e("CartRequest", errorMsg, error);
                    callback.onError(errorMsg);
                }
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                String token = AuthRequest.getAccessToken(context);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(request);
    }

}
