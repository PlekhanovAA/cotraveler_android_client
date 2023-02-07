/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.rest;

@FunctionalInterface
public interface ErrorCallBack {
    void onError(Exception error);
}
