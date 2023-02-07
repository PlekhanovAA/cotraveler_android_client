/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.rest;

import android.graphics.Bitmap;

@FunctionalInterface
public interface BitmapCallBack {
    void onSuccess(Bitmap result);
}
