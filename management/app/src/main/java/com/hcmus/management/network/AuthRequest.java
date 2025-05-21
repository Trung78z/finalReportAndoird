package com.hcmus.management.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmus.management.common.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class AuthRequest {

    public interface Callback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }

    public static void login(
            Context context,
            RequestQueue requestQueue,
            String email,
            String password,
            Callback callback
    ) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Api.apiLogin,
                    requestBody,
                    response -> {
                        // Handle successful response (status code 200)
                        try {
                            if (response.getBoolean("success")) {
                                JSONObject msg = response.getJSONObject("msg");
                                String accessToken = msg.getString("accessToken");

                                SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                                prefs.edit().putString("ACCESS_TOKEN", accessToken).apply();

                                callback.onSuccess(response);
                            } else {
                                String errorMsg = response.getString("msg");
                                callback.onError(errorMsg);
                            }
                        } catch (JSONException e) {
                            callback.onError("Error parsing response");
                        }
                    },
                    error -> {
                        // Handle error response (status code 400, etc.)
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String jsonString = new String(error.networkResponse.data,
                                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                                JSONObject errorResponse = new JSONObject(jsonString);
                                callback.onError(errorResponse.getString("msg"));
                            } catch (Exception e) {
                                callback.onError("Invalid email or password");
                            }
                        } else {
                            callback.onError(error.getMessage());
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            callback.onError("Error creating login request");
        }
    }


    public static void refreshToken(
            Context context,
            RequestQueue requestQueue,
            Callback callback
    ) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("REFRESH_TOKEN", null);

        if (refreshToken == null) {
            callback.onError("No refresh token available");
            return;
        }

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Api.apiRefreshToken,
                    null, // No body needed if refresh token is in cookies
                    response -> callback.onSuccess(response),
                    error -> callback.onError(error.getMessage())
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");

                    return headers;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        JSONObject jsonResponse = new JSONObject(jsonString);

                        // Store new access token
                        JSONObject msg = jsonResponse.getJSONObject("msg");
                        String newAccessToken = msg.getString("accessToken");

                        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("ACCESS_TOKEN", newAccessToken).apply();

                        return Response.success(jsonResponse,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (Exception e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };

            requestQueue.add(request);
        } catch (Exception e) {
            callback.onError("Refresh error: " + e.getMessage());
        }
    }


    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("ACCESS_TOKEN", null);

        if (accessToken == null) {
            return false;
        }

        // Check if token is expired
        long expirationTime = prefs.getLong("TOKEN_EXPIRATION", 0);
        return expirationTime == 0 || System.currentTimeMillis() < expirationTime;
    }

    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("ACCESS_TOKEN");
        editor.remove("REFRESH_TOKEN");
        editor.remove("COOKIES");
        editor.remove("TOKEN_EXPIRATION");
        editor.apply();

        // Clear cookies from CookieManager if used
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.getCookieStore().removeAll();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return prefs.getString("ACCESS_TOKEN", null);
    }
}