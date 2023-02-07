/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.rest;

import org.json.JSONObject;

@FunctionalInterface
public interface JsonObjectCallBack {
    void onSuccess(JSONObject result);
}
