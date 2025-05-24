package com.hcmus.management.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface Callback {
    void onSuccess(JSONObject response);

    void onError(Object message);
}