/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.utils;

import com.cotraveler.androidapp.entities.Offer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSONObjectBuilder {
    final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    Map<String, String> jsonMap;

    public JSONObjectBuilder() {
        jsonMap = new HashMap<>();
    }

    public JSONObjectBuilder addKeyValue(String key, String value) {
        this.jsonMap.put(key, value);
        return this;
    }

    public JSONObject buildJSONObject() {
        return new JSONObject(jsonMap);
    }

    public static JSONObject offerToJson(Offer offer) throws JSONException {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        return new JSONObject(gson.toJson(offer));
    }

    public static Offer jsonToOffer(JSONObject json) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        return gson.fromJson(json.toString(), Offer.class);
    }
}
