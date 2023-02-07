/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cotraveler.androidapp.R;

public class OfferCommentFragment extends Fragment {
    final static String CREATE_OFFER_FRAGMENT_TAG = "CREATE_OFFER_FRAGMENT";
    final static String OFFER_COMMENT_FRAGMENT_TAG = "OFFER_COMMENT_FRAGMENT";

    EditText commentEditText;
    Button applyCommentButton, backButton;

    FragmentManager fragmentManager;
    OfferCommentFragment thisOfferCommentFragment;
    CreateOfferFragment createOfferFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_offer_passenger, null);

        fragmentManager = getParentFragmentManager();
        thisOfferCommentFragment = (OfferCommentFragment) fragmentManager.findFragmentByTag(OFFER_COMMENT_FRAGMENT_TAG);

        createCommentEditText(view);
        createButtons(view);

        return view;
    }

    private void createCommentEditText(View view) {
        commentEditText = view.findViewById(R.id.commentEditTextId);
    }

    private void createButtons(View view) {
        applyCommentButton = view.findViewById(R.id.applyCommentButtonId);
        applyCommentButton.setOnClickListener(v -> {
            thisOfferCommentFragment = (OfferCommentFragment) fragmentManager.findFragmentByTag(OFFER_COMMENT_FRAGMENT_TAG);
            fragmentManager.beginTransaction().
                    hide(thisOfferCommentFragment).
                    commit();
            createOfferFragment = (CreateOfferFragment) fragmentManager.findFragmentByTag(CREATE_OFFER_FRAGMENT_TAG);
            createOfferFragment.getCurrentOffer().setComment(commentEditText.getText().toString());
            fragmentManager.beginTransaction().
                    show(createOfferFragment).
                    commit();
        });

        backButton = view.findViewById(R.id.backButtonId);
        backButton.setOnClickListener(v -> {
            thisOfferCommentFragment = (OfferCommentFragment) fragmentManager.findFragmentByTag(OFFER_COMMENT_FRAGMENT_TAG);
            fragmentManager.beginTransaction().
                    hide(thisOfferCommentFragment).
                    commit();
            createOfferFragment = (CreateOfferFragment) fragmentManager.findFragmentByTag(CREATE_OFFER_FRAGMENT_TAG);
            fragmentManager.beginTransaction().
                    show(createOfferFragment).
                    commit();
        });
    }

}
