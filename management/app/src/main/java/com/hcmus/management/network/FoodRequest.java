package com.hcmus.management.network;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmus.management.common.Api;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FoodRequest {

    public interface Callback {
        void onSuccess();

        void onError(String message);
    }

    public static void sendFoodItem(
            Context context,
            RequestQueue requestQueue,
            Uri selectedImageUri,
            String name,
            double price,
            String description,
            Callback callback
    ) {
        try {
            // Convert image to Base64
            InputStream inputStream = context.getContentResolver().openInputStream(selectedImageUri);
            byte[] bytes = getBytes(inputStream);
            String imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

            // Create JSON request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("name", name);
            requestBody.put("price", price);
            requestBody.put("description", description);
            requestBody.put("image", imageBase64);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    Api.createFood,
                    requestBody,
                    response -> callback.onSuccess(),
                    error -> callback.onError(error.toString())
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + AuthRequest.getAccessToken(context));
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onError("Error preparing data: " + e.getMessage());
        }
    }

    private static byte[] getBytes(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }
}
