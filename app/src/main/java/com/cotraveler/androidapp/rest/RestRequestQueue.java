/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.rest;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RestRequestQueue {
    RequestQueue requestQueue;

    public RestRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }

    public void addGetJsonArrayRequest(String url,
                                       final JsonArrayCallBack callback,
                                       final ErrorCallBack errorCallBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                callback::onSuccess,
                errorCallBack::onError) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        addToRequestQueue(jsonArrayRequest);
    }

    public void addGetJsonObjectRequest(String url,
                                        final JsonObjectCallBack successCallback,
                                        final ErrorCallBack errorCallBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                successCallback::onSuccess,
                errorCallBack::onError) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        addToRequestQueue(jsonObjectRequest);
    }

    public void addGetImageRequest(String url,
                                   final BitmapCallBack callback,
                                   final ErrorCallBack errorCallBack) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                callback::onSuccess,
                1000,
                1000,
                null,
                null,
                errorCallBack::onError);
        addToRequestQueue(imageRequest);
    }

    public void addPostJsonObjectRequest(String url, JSONObject params,
                                         final JsonObjectCallBack callback,
                                         final ErrorCallBack errorCallBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                callback::onSuccess,
                errorCallBack::onError) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        addToRequestQueue(jsonObjectRequest);
    }

    private void addToRequestQueue(Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
