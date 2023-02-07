/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cotraveler.androidapp.activities.OfferActivity;
import com.cotraveler.androidapp.entities.Offer;
import com.cotraveler.androidapp.rest.RestRequestQueue;
import com.cotraveler.androidapp.utils.DialogHelper;
import com.cotraveler.androidapp.utils.JSONObjectBuilder;
import com.cotraveler.androidapp.utils.URIHelper;
import com.cotraveler.androidapp.R;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

public class CreateOfferFragment extends Fragment {
    final static String CREATE_OFFER_FRAGMENT_TAG = "CREATE_OFFER_FRAGMENT";
    final static String OFFER_COMMENT_FRAGMENT_TAG = "OFFER_COMMENT_FRAGMENT";

    RestRequestQueue restRequestQueue;

    RadioButton fromRadioButton, toRadioButton;
    TextView fromText, toText;
    Button submitButton, addCommentButton;

    FragmentManager fragmentManager;
    CreateOfferFragment thisCreateOfferFragment;
    OfferCommentFragment offerCommentFragment;

    Offer currentOffer;
    long offerLifeTimeMin;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_offer_passenger, null);

        restRequestQueue = new RestRequestQueue(view.getContext());

        fragmentManager = getParentFragmentManager();

        currentOffer = new Offer.OfferBuilder().build();
        setOfferLifetime();

        createSubmitButton();
        createRadioGroup();
        createAddCommentButton();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        finishAllOffersByClientId();
    }

    public void setMarkerInfo(Marker marker) {
        if (fromRadioButton.isChecked()) {
            fromText.setText(marker.getTitle());
            currentOffer.setFromLabelName(marker.getTitle());
        } else if (toRadioButton.isChecked()) {
            toText.setText(marker.getTitle());
            currentOffer.setToLabelName(marker.getTitle());
        }
    }

    private void setOfferLifetime() {
        restRequestQueue.addGetJsonObjectRequest(
                URIHelper.getInstance().offerLifetimeEndPoint(),
                result -> {
                    try {
                        offerLifeTimeMin = Long.parseLong(result.get("offerLifetime").toString());
                    } catch (JSONException e) {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.ERROR, view.getContext(),
                                "Invalid JSON Object: " + e);
                    }
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, view.getContext(),"setOfferLifetime: ", error);
                }
        );
    }

    private void finishAllOffersByClientId() {
        @SuppressLint("HardwareIds")
        String clientId = Settings.Secure.getString(view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        restRequestQueue.addPostJsonObjectRequest(
                URIHelper.getInstance().finishAllOffersEndPoint(),
                new JSONObjectBuilder().addKeyValue("clientId", clientId).buildJSONObject(),
                result -> {
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, view.getContext(), "finishAllOffersByClientId: ", error);
                }
        );
    }

    private void createSubmitButton() {
        submitButton = view.findViewById(R.id.submitButtonId);
        submitButton.setOnClickListener(v -> {
            checkOfferInfo();
        });
    }

    private void checkOfferInfo() {
        if (currentOffer.getFromLabelName() != null && currentOffer.getToLabelName() != null &&
                !currentOffer.getFromLabelName().equals(currentOffer.getToLabelName())) {
            checkAvailabilityLabelFrom();
        } else {
            DialogHelper.getInstance().showDialog(
                    DialogHelper.DialogType.WARNING, view.getContext(),
                    R.string.warningIncompleteOfferLabelText);
        }
    }

    private void checkAvailabilityLabelFrom() {
        restRequestQueue.addGetJsonObjectRequest(
                URIHelper.getInstance().availabilityLabelEndPoint(currentOffer.getFromLabelName()),
                result -> {
                    try {
                        boolean checkResult = Boolean.parseBoolean(result.get("availabilityLabel").toString());
                        if (checkResult) {
                            createOffer();
                        } else {
                            DialogHelper.getInstance().showDialog(
                                    DialogHelper.DialogType.WARNING, view.getContext(),
                                    R.string.warningAvailabilityLabelText);
                        }
                    } catch (JSONException e) {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.ERROR, view.getContext(),
                                "Invalid JSON Object: " + e);
                    }
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, view.getContext(), "checkAvailabilityLabelFrom: ", error);
                }
        );
    }

    private void createOffer() throws JSONException {
        @SuppressLint("HardwareIds")
        String clientId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        currentOffer.setClientId(clientId);
        restRequestQueue.addPostJsonObjectRequest(
                URIHelper.getInstance().startOfferEndPoint(),
                JSONObjectBuilder.offerToJson(currentOffer),
                result -> {
                    currentOffer = JSONObjectBuilder.jsonToOffer(result);
                    Intent intent = new Intent(getActivity(), OfferActivity.class);
                    intent.putExtra("currentOffer", currentOffer);
                    intent.putExtra("offerLifeTimeMin", offerLifeTimeMin);
                    startActivity(intent);
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, view.getContext(), "createOffer: ", error);
                }
        );
    }

    private void createRadioGroup() {
        fromRadioButton = view.getRootView().findViewById(R.id.fromRadioButtonId);
        toRadioButton = view.getRootView().findViewById(R.id.toRadioButtonId);
        fromText = view.getRootView().findViewById(R.id.fromTextInRadioGroupId);
        toText = view.getRootView().findViewById(R.id.toTextInRadioGroupId);
    }

    private void createAddCommentButton() {
        addCommentButton = view.getRootView().findViewById(R.id.addCommentToOfferButtonId);
        addCommentButton.setOnClickListener(v -> {
            thisCreateOfferFragment = (CreateOfferFragment) fragmentManager.findFragmentByTag(CREATE_OFFER_FRAGMENT_TAG);
            fragmentManager.beginTransaction().
                    hide(thisCreateOfferFragment).
                    commit();
            offerCommentFragment = (OfferCommentFragment) fragmentManager.findFragmentByTag(OFFER_COMMENT_FRAGMENT_TAG);
            if (offerCommentFragment == null) {
                offerCommentFragment = new OfferCommentFragment();
                fragmentManager.beginTransaction().
                        add(R.id.createOfferLayoutId, offerCommentFragment, OFFER_COMMENT_FRAGMENT_TAG).
                        commit();
            } else {
                fragmentManager.beginTransaction().
                        show(offerCommentFragment).
                        commit();
            }
        });
    }

    public Offer getCurrentOffer() {
        return currentOffer;
    }

    public void setCurrentOffer(Offer currentOffer) {
        this.currentOffer = currentOffer;
    }

    public RadioButton getFromRadioButton() {
        return fromRadioButton;
    }

    public void setFromRadioButton(RadioButton fromRadioButton) {
        this.fromRadioButton = fromRadioButton;
    }

    public RadioButton getToRadioButton() {
        return toRadioButton;
    }

    public void setToRadioButton(RadioButton toRadioButton) {
        this.toRadioButton = toRadioButton;
    }

    public TextView getFromText() {
        return fromText;
    }

    public void setFromText(TextView fromText) {
        this.fromText = fromText;
    }

    public TextView getToText() {
        return toText;
    }

    public void setToText(TextView toText) {
        this.toText = toText;
    }

}
