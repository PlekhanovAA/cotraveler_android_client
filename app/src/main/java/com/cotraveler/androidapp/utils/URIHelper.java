/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.utils;

import android.net.Uri;

public class URIHelper {
    private static URIHelper single_instance = null;

    private Uri.Builder builder;

    private URIHelper() {

    }

    public static URIHelper getInstance() {
        if (single_instance == null) {
            single_instance = new URIHelper();
        }

        return single_instance;
    }

    public String initVersionValuesEndPoint() {
        createBuilder();
        addAppPartPath();
        return builder.
                appendPath("mobileAppVer").
                build().
                toString();
    }

    public String possibilityPassengerEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("possibilityPassenger").
                build().
                toString();
    }

    public String finishOfferEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("finishOffer").
                build().
                toString();
    }

    public String bannerEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("getBanner").
                build().
                toString();
    }

    public String offerLifetimeEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("offerLifetime").
                build().
                toString();
    }

    public String finishAllOffersEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("finishAllOffers").
                build().
                toString();
    }

    public String startOfferEndPoint() {
        createBuilder();
        addPassengerPartPath();
        return builder.
                appendPath("startOffer").
                build().
                toString();
    }

    public String labelsEndPoint(String cityName) {
        createBuilder();
        addMapPartPath();
        return builder.
                appendPath("city").
                appendQueryParameter("cityName", cityName).
                build().
                toString();
    }

    public String availabilityLabelEndPoint(String labelName) {
        createBuilder();
        addMapPartPath();
        return builder.
                appendPath("availabilityLabel").
                appendQueryParameter("labelName", labelName).
                build().
                toString();
    }

    public String offersForMarkerEndPoint(String labelName) {
        createBuilder();
        addDriverPartPath();
        return builder.
                appendPath("labelOffers").
                appendQueryParameter("labelName", labelName).
                build().
                toString();
    }



    private void createBuilder() {
        builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority("79.133.181.96:8080");
    }

    private void addPassengerPartPath() {
        builder.appendPath("passenger");
    }

    private void addDriverPartPath() {
        builder.appendPath("driver");
    }

    private void addMapPartPath() {
        builder.appendPath("map");
    }

    private void addAppPartPath() {
        builder.appendPath("app");
    }

}
