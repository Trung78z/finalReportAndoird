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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmus.management.common.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthRequest {
    private static final String TAG = "AuthRequest";
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN";

    public interface Callback {
        void onSuccess(JSONObject response);
        void onError(String message);
    }


    public static void initCookieManager(Context context) {
        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);
            Log.d(TAG, "CookieManager initialized");
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize CookieManager", e);
        }
    }

    public static void login(Context context, RequestQueue requestQueue,
                             String email, String password, Callback callback) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("email", email);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Api.apiLogin,
                    requestBody,
                    response -> handleLoginResponse(context, response, callback),
                    error -> handleLoginError(error, callback)
            );

            requestQueue.add(request);
        } catch (JSONException e) {
            callback.onError("Invalid login request format");
        }
    }

    private static void handleLoginResponse(Context context, JSONObject response, Callback callback) {
        try {
            if (response.getBoolean("success")) {
                String accessToken = response.getJSONObject("msg").getString("accessToken");
                saveAccessToken(context, accessToken);
                saveRefreshToken(context);
                callback.onSuccess(response);
            } else {
                callback.onError(response.getString("msg"));
            }
        } catch (JSONException e) {
            callback.onError("Invalid login response format");
        }
    }

    private static void handleLoginError(VolleyError error, Callback callback) {
        String errorMsg = "Login failed";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String jsonString = new String(error.networkResponse.data,
                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                JSONObject errorResponse = new JSONObject(jsonString);
                errorMsg = errorResponse.optString("msg", errorMsg);
            } catch (Exception e) {
                errorMsg = "Invalid credentials";
            }
        }
        callback.onError(errorMsg);
    }

    public static void refreshToken(Context context, RequestQueue requestQueue, Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Api.apiRefreshToken,
                null,
                response -> handleRefreshResponse(context, response, callback),
                error -> handleRefreshError(error, callback)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private static void handleRefreshResponse(Context context, JSONObject response, Callback callback) {
        try {
            if (response.getBoolean("success")) {
                String newAccessToken = response.getJSONObject("msg").getString("accessToken");
                saveAccessToken(context, newAccessToken);
                saveRefreshToken(context);
                callback.onSuccess(response);
            } else {
                callback.onError(response.getString("msg"));
            }
        } catch (JSONException e) {
            callback.onError("Invalid refresh response format");
        }
    }

    private static void handleRefreshError(VolleyError error, Callback callback) {
        String errorMsg = "Refresh token failed";
        if (error.networkResponse != null) {
            try {
                String jsonString = new String(error.networkResponse.data,
                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                JSONObject errorResponse = new JSONObject(jsonString);
                errorMsg = errorResponse.optString("msg", errorMsg);
            } catch (Exception e) {
                errorMsg = "Invalid server response";
            }
        }
        callback.onError(errorMsg);
    }

    private static void saveAccessToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(ACCESS_TOKEN_KEY, token).apply();
    }

    private static void saveRefreshToken(Context context) {
        String refreshToken = getRefreshToken();
        if (refreshToken != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply();
        }
    }

    public static String getAccessToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(ACCESS_TOKEN_KEY, null);
    }

    public static String getRefreshToken() {
        try {
            CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
            if (cookieManager != null) {
                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                for (HttpCookie cookie : cookies) {
                    if ("refreshToken".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to get refresh token", e);
        }
        return null;
    }


    public static String getSavedRefreshToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(REFRESH_TOKEN_KEY, null);
    }

    public static void logout(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(ACCESS_TOKEN_KEY).apply();


        clearCookies();
    }

    private static void clearCookies() {
        try {
            CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
            if (cookieManager != null) {
                cookieManager.getCookieStore().removeAll();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear cookies", e);
        }
    }

    public static void restoreRefreshToken(Context context) {
        String refreshToken = getSavedRefreshToken(context);
        if (refreshToken != null) {
            try {
                CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
                HttpCookie cookie = new HttpCookie("refreshToken", refreshToken);
                cookie.setPath("/");
                cookie.setHttpOnly(true);


                cookieManager.getCookieStore().add(null, cookie);
                Log.d(TAG, "Refresh token restored to CookieManager");
            } catch (Exception e) {
                Log.e(TAG, "Failed to restore refresh token", e);
            }
        }
    }
}