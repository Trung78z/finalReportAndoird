package com.hcmus.management.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcmus.management.common.Api;
import com.hcmus.management.dto.PaymentDTO;
import com.hcmus.management.model.CartItem;
import com.hcmus.management.model.Category;
import com.hcmus.management.model.FoodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartRequest {
	
	
	public interface Callback {
		void onSuccess(JSONObject response);
		
		void onError(String message);
	}
	
	public static void payment(Context context, RequestQueue requestQueue,
							   PaymentDTO paymentDTO, Callback callback) {
		try {
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(gson.toJson(paymentDTO));
			Log.d("checcccccccc",jsonObject.toString());
			JsonObjectRequest request = new JsonObjectRequest(
					Request.Method.POST,
					Api.payment,
					jsonObject,
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
	
	public static void order(Context context, RequestQueue requestQueue,
							 String foodId, Integer quantity, Callback callback) {
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

	public static void updateCart(Context context, CartItem item, CartRequest.Callback callback) {
		try {
			RequestQueue requestQueue = Volley.newRequestQueue(context);
			String url = Api.updateCart + "/" + item.getId(); // Assuming RESTful endpoint: /foods/{id}

			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(gson.toJson(item));
			JsonObjectRequest request = new JsonObjectRequest(
					Request.Method.PUT,
					url,
					jsonObject,
					response -> {
						callback.onSuccess(response);

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
		} catch (JSONException e) {
		}

	}
	
	public static void deleteCart(Context context, Integer cartId, CartRequest.Callback callback) {
		RequestQueue requestQueue = Volley.newRequestQueue(context);
		String url = Api.deleteCart + "/" + cartId; // Assuming RESTful endpoint: /foods/{id}
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
	
	private static void onSuccess(Context context, JSONObject response, Callback callback) {
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
	
	private static void onError(VolleyError error, Callback callback) {
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
						JSONArray arr = response.getJSONArray("msg");
						for (int i = 0; i < arr.length(); i++) {
							JSONObject obj = arr.getJSONObject(i);
							JSONObject foodObj = obj.getJSONObject("food");
							String id = foodObj.optString("id", "");
							String name = foodObj.optString("name", "");
							String description = foodObj.optString("description", "");
							double price = foodObj.optDouble("price", 0);
							String imageUrl = foodObj.optString("imageUrl", "");
							int cartId = obj.optInt("id", 0);
							
							int quantity = obj.optInt("quantity", 1);
							
							FoodItem food = new FoodItem(id, name, description, price, quantity, Api.baseUrl + imageUrl, cartId);
							cartItems.add(new CartItem(cartId, quantity, food));
						}
						callback.onSuccess(cartItems);
					} catch (JSONException e) {
						callback.onError("Parse error");
					}
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
	
}
