/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cotraveler.androidapp.utils.MapHelper;
import com.cotraveler.androidapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
    final static String CREATE_OFFER_FRAGMENT_TAG = "CREATE_OFFER_FRAGMENT";

    FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);

        fragmentManager = getParentFragmentManager();

        createMap();

        return view;
    }

    private void createMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.passengerMapId);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(map -> {
                for (MarkerOptions markerOptions : MapHelper.getInstance().getLabelMarkers()) {
                    map.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        MapHelper.getInstance().getCityMarker(),
                        MapHelper.getInstance().getCameraZoom()));
                map.setMinZoomPreference(MapHelper.getInstance().getMinCameraZoom());
                map.setMaxZoomPreference(MapHelper.getInstance().getMaxCameraZoom());
                LatLngBounds cityBounds = new LatLngBounds(
                        MapHelper.getInstance().getCitySWBound(), // SW bounds
                        MapHelper.getInstance().getCityNEBound()  // NE bounds
                );
                map.setLatLngBoundsForCameraTarget(cityBounds);
                CreateOfferFragment createOfferFragment = (CreateOfferFragment) fragmentManager.findFragmentByTag(CREATE_OFFER_FRAGMENT_TAG);
                if (createOfferFragment != null) {
                    map.setOnMarkerClickListener(marker -> {
                        createOfferFragment.setMarkerInfo(marker);
                        return true;
                    });
                }
            });
        }
    }

}
