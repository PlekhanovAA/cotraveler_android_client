/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;

import com.cotraveler.androidapp.R;
import com.cotraveler.androidapp.activities.fragments.CreateOfferFragment;
import com.cotraveler.androidapp.activities.fragments.MapFragment;
import com.cotraveler.androidapp.databinding.ActivityPassengerBinding;
import com.cotraveler.androidapp.utils.LocaleHelper;

public class PassengerActivity extends FragmentActivity {
    final static String MAP_FRAGMENT_TAG = "MAP_FRAGMENT";
    final static String CREATE_OFFER_FRAGMENT_TAG = "CREATE_OFFER_FRAGMENT";


    FragmentManager fragmentManager;
    MapFragment mapFragment;
    CreateOfferFragment createOfferFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPassengerBinding binding = ActivityPassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createFragments();
    }

    private void createFragments() {
        fragmentManager = getSupportFragmentManager();
        mapFragment = new MapFragment();
        fragmentManager.beginTransaction().
                add(R.id.mapLayoutId, mapFragment, MAP_FRAGMENT_TAG).
                commit();
        createOfferFragment = new CreateOfferFragment();
        fragmentManager.beginTransaction().
                add(R.id.createOfferLayoutId, createOfferFragment, CREATE_OFFER_FRAGMENT_TAG).
                commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}