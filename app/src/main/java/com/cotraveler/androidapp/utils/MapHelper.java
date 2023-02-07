/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.utils;

import android.content.Context;

import com.cotraveler.androidapp.rest.RestRequestQueue;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapHelper {
    private static MapHelper single_instance = null;

    private final static String CITY_NAME = "AKTAU";
    private LatLng cityMarker;
    private final List<MarkerOptions> labelMarkers = new ArrayList<>();
    private LatLng cityNEBound;
    private LatLng citySWBound;
    private float cameraZoom;
    private float minCameraZoom;
    private float maxCameraZoom;

    private boolean mapIsReady = false;

    private MapHelper() {

    }

    public static MapHelper getInstance() {
        if (single_instance == null)
            single_instance = new MapHelper();

        return single_instance;
    }

    public void getLabels(RestRequestQueue restRequestQueue, Context context) {
        restRequestQueue.addGetJsonObjectRequest(
                URIHelper.getInstance().labelsEndPoint(CITY_NAME),
                result -> {
                    try {
                        cityMarker = new LatLng(
                                result.getDouble("cityLatitude"),
                                result.getDouble("cityLongitude"));
                        JSONArray arr = result.getJSONArray("cityLabels");
                        for (int j = 0; j < arr.length(); j++) {
                            JSONObject obj = arr.getJSONObject(j);
                            labelMarkers.add(new MarkerOptions().
                                    position(new LatLng(obj.getDouble("labelLatitude"), obj.getDouble("labelLongitude"))).
                                    title(obj.getString("labelName")));
                        }
                        JSONObject jsonObject = result.getJSONObject("cityNEBound");
                        cityNEBound = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                        jsonObject = result.getJSONObject("citySWBound");
                        citySWBound = new LatLng(jsonObject.getDouble("latitude"), jsonObject.getDouble("longitude"));
                        jsonObject = result.getJSONObject("cameraZoom");
                        cameraZoom = Float.parseFloat(jsonObject.get("zoom").toString());
                        minCameraZoom = Float.parseFloat(jsonObject.get("minZoom").toString());
                        maxCameraZoom = Float.parseFloat(jsonObject.get("maxZoom").toString());
                        mapIsReady = true;
                    } catch (JSONException e) {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.ERROR, context,
                                "Invalid JSON Object: " + e);
                        mapIsReady = false;
                    }
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, context, "getLabels: ", error);
                    mapIsReady = false;
                }
        );
    }

    public MarkerOptions findByTitle(String title) {
        for (int i = 0; i < labelMarkers.size(); i++) {
            if (labelMarkers.get(i).getTitle().equals(title)) {
                return labelMarkers.get(i);
            }
        }
        return null;
    }

    public boolean mapIsReady(Context context) {
        if (!mapIsReady) {
            DialogHelper.getInstance().showDialog(
                    DialogHelper.DialogType.ERROR, context,
                    "The map is not ready. Please restart the application.");
            return false;
        }
        return true;
    }

    public LatLng getCityMarker() {
        return cityMarker;
    }

    public List<MarkerOptions> getLabelMarkers() {
        return labelMarkers;
    }

    public LatLng getCityNEBound() {
        return cityNEBound;
    }

    public LatLng getCitySWBound() {
        return citySWBound;
    }

    public float getCameraZoom() {
        return cameraZoom;
    }

    public float getMinCameraZoom() {
        return minCameraZoom;
    }

    public float getMaxCameraZoom() {
        return maxCameraZoom;
    }

}
