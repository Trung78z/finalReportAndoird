package com.hcmus.management.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hcmus.management.common.Api;
import com.hcmus.management.model.FoodItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class FoodRequest {

    private static final String TAG = "FoodRequest";
    private static final int MAX_IMAGE_SIZE = 1024; // Max width/height in pixels
    private static final int JPEG_QUALITY = 80; // Compression quality (0-100)
    private static final long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

    public interface Callback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }

    public interface FoodListCallback {
        void onSuccess(List<FoodItem> foodItems);

        void onError(String message);
    }

    public static void sendFoodItem(Context context,
                                    Uri selectedImageUri,
                                    String name,
                                    double price,
                                    String description,
                                    int categoryId,
                                    Callback callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        try {
            // 1. Process the image
            byte[] imageBytes = processImage(context, selectedImageUri);
            if (imageBytes == null) {
                callback.onError("Failed to process image");
                return;
            }

            // 2. Create request body
            JSONObject requestBody = createRequestBody(name, price, description, categoryId, imageBytes);

            // 3. Create and send request
            JsonObjectRequest request = createRequest(requestBody, callback, context);
            requestQueue.add(request);

        } catch (Exception e) {
            Log.e(TAG, "Error in sendFoodItem", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    public static void fetchFoodList(Context context, FoodListCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Api.getFoodList, // Make sure this is your GET endpoint for food list
                null,
                response -> {
                    List<FoodItem> foodItems = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String id = obj.optString("id", "");
                            String name = obj.getString("name");
                            String description = obj.optString("description", "");
                            double price = obj.getDouble("price");
                            String imageUrl = obj.optString("imageUrl", "");
                            int categoryId = obj.getJSONObject("category").getInt("id");
                            FoodItem item = new FoodItem(id, name, description, price, 1, Api.baseUrl + imageUrl, categoryId);
                            foodItems.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onSuccess(foodItems);
                },
                error -> {
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " (code: " + error.networkResponse.statusCode + ")";
                    }
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

    public static void deleteFood(Context context, String foodId, Callback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = Api.getFoodList + "/" + foodId; // Assuming RESTful endpoint: /foods/{id}
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    callback.onSuccess(response);

                },
                error -> {
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " (code: " + error.networkResponse.statusCode + ")";
                    }
                    Log.e(TAG, errorMsg, error);
                    callback.onError(errorMsg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
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

    private static byte[] processImage(Context context, Uri imageUri) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri)) {
            if (inputStream == null) {
                Log.e(TAG, "Could not open image stream");
                return null;
            }

            // Decode with options to reduce memory usage
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            // Calculate sampling to reduce image size
            options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
            options.inJustDecodeBounds = false;

            // Need to re-open the stream
            try (InputStream newStream = context.getContentResolver().openInputStream(imageUri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(newStream, null, options);
                if (bitmap == null) {
                    Log.e(TAG, "Failed to decode bitmap");
                    return null;
                }

                // Compress to JPEG
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, baos);
                byte[] imageBytes = baos.toByteArray();

                // Check size limit
                if (imageBytes.length > MAX_IMAGE_SIZE_BYTES) {
                    Log.w(TAG, "Image size too large, compressing further");
                    return compressImageFurther(bitmap);
                }

                return imageBytes;
            }
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private static byte[] compressImageFurther(Bitmap bitmap) {
        int quality = JPEG_QUALITY;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        do {
            baos.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        } while (baos.size() > MAX_IMAGE_SIZE_BYTES && quality > 10);

        return baos.toByteArray();
    }

    private static JSONObject createRequestBody(String name,
                                                double price,
                                                String description,
                                                int categoryId,
                                                byte[] imageBytes) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        requestBody.put("price", price);
        requestBody.put("description", description);
        requestBody.put("categoryId", categoryId);
        requestBody.put("image", Base64.encodeToString(imageBytes, Base64.NO_WRAP));
        return requestBody;
    }

    private static JsonObjectRequest createRequest(JSONObject requestBody,
                                                   Callback callback,
                                                   Context context) {
        return new JsonObjectRequest(
                Request.Method.POST,
                Api.createFood,
                requestBody,
                response -> {
                    Log.d(TAG, "Response: " + response.toString());
                    callback.onSuccess(response);
                },
                error -> {
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " (code: " + error.networkResponse.statusCode + ")";
                    }
                    Log.e(TAG, errorMsg, error);
                    callback.onError(errorMsg);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = AuthRequest.getAccessToken(context);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

}