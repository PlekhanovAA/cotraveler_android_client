/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cotraveler.androidapp.utils.JSONObjectBuilder;
import com.cotraveler.androidapp.utils.MapHelper;
import com.cotraveler.androidapp.R;
import com.cotraveler.androidapp.adapters.AdapterOffer;
import com.cotraveler.androidapp.entities.Offer;
import com.cotraveler.androidapp.rest.RestRequestQueue;
import com.cotraveler.androidapp.utils.DialogHelper;
import com.cotraveler.androidapp.utils.LocaleHelper;
import com.cotraveler.androidapp.utils.URIHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.cotraveler.androidapp.databinding.ActivityDriverBinding;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends FragmentActivity implements OnMapReadyCallback {

    RestRequestQueue restRequestQueue;

    ListView offerList;
    TextView headerOfferList;
    List<Offer> offers = new ArrayList<>();
    AdapterOffer offerListAdapter;

    Button resetOfferListButton;

    GoogleMap googleMap;
    MarkerOptions selectedMarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDriverBinding binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        offerListAdapter = new AdapterOffer(this, offers);
        restRequestQueue = new RestRequestQueue(this);
        createMap();
        createOfferList();
        createResetOfferListButton();
    }

    private void createMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.driverMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void createOfferList() {
        offerList = findViewById(R.id.offerListViewId);
        offerList.setOnItemClickListener((parent, itemClicked, position, id) -> {
            googleMap.clear();
            googleMap.addMarker(selectedMarkerOptions);
            googleMap.addMarker(MapHelper.getInstance().findByTitle(offers.get((int) id).getToLabelName()).
                    title(offers.get((int) id).getToLabelName()).
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            googleMap.setOnMarkerClickListener(marker -> true);
        });
        headerOfferList = findViewById(R.id.offerListViewHeaderId);
        headerOfferList.setText(R.string.headerOfferListBeforeSelectLabelText);

        offerList.setAdapter(offerListAdapter);
    }

    private void createResetOfferListButton() {
        resetOfferListButton = findViewById(R.id.resetOfferListButtonId);
        resetOfferListButton.setOnClickListener(e -> {
            setMapDefaultState();
            offers.clear();
            offerListAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        setMapDefaultState();
    }

    private void setMapDefaultState() {
        setMapDefaultStateWithResetZoom(true);
    }

    private void setMapDefaultStateWithResetZoom(boolean resetZoom) {
        for (MarkerOptions markerOptions : MapHelper.getInstance().getLabelMarkers()) {
            googleMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

        googleMap.setOnMarkerClickListener(marker -> {
            selectedMarkerOptions = new MarkerOptions().
                    position(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)).
                    title(marker.getTitle());
            getOffersForMarker(marker.getTitle());
            return true;
        });

        if (resetZoom) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    MapHelper.getInstance().getCityMarker(),
                    MapHelper.getInstance().getCameraZoom()));
        }

        googleMap.setMinZoomPreference(MapHelper.getInstance().getMinCameraZoom());
        googleMap.setMaxZoomPreference(MapHelper.getInstance().getMaxCameraZoom());

        LatLngBounds cityBounds = new LatLngBounds(
                MapHelper.getInstance().getCitySWBound(), // SW bounds
                MapHelper.getInstance().getCityNEBound()  // NE bounds
        );
        googleMap.setLatLngBoundsForCameraTarget(cityBounds);

        headerOfferList.setText(R.string.headerOfferListBeforeSelectLabelText);
    }

    private void getOffersForMarker(String labelName) {
        restRequestQueue.addGetJsonArrayRequest(
                URIHelper.getInstance().offersForMarkerEndPoint(labelName),
                result -> {
                    offers.clear();
                    if (result.length() > 0) {
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                offers.add(JSONObjectBuilder.jsonToOffer(result.getJSONObject(i)));
                                headerOfferList.setText(R.string.headerOfferListAfterSelectLabelText);
                            } catch (JSONException e) {
                                DialogHelper.getInstance().showDialog(
                                        DialogHelper.DialogType.ERROR, this,
                                        "Invalid JSON Object: " + e);
                            }
                        }
                    } else {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.WARNING, this,
                                R.string.warningForOfferListWhenNoOrdersText);
                        setMapDefaultStateWithResetZoom(false);
                    }
                    offerListAdapter.notifyDataSetChanged();
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, this, "getOffersForMarker: ", error);
                }
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}