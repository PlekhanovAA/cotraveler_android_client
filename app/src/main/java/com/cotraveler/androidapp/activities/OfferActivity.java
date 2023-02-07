/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cotraveler.androidapp.databinding.ActivityOfferBinding;
import com.cotraveler.androidapp.entities.Offer;
import com.cotraveler.androidapp.rest.RestRequestQueue;
import com.cotraveler.androidapp.utils.DialogHelper;
import com.cotraveler.androidapp.utils.JSONObjectBuilder;
import com.cotraveler.androidapp.utils.LocaleHelper;
import com.cotraveler.androidapp.utils.URIHelper;
import com.cotraveler.androidapp.R;

import org.json.JSONException;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OfferActivity extends FragmentActivity {

    int offerCounterTimeSec = 1000 * 10;

    RestRequestQueue restRequestQueue;

    TextView offerInfoFromTextView, offerInfoToTextView, offerCounterTextView;
    Button doneButton;
    CountDownTimer countDownTimer;

    FragmentManager fragmentManager;

    Offer currentOffer;
    long offerLifeTimeMin;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOfferBinding binding = ActivityOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        restRequestQueue = new RestRequestQueue(context);

        fragmentManager = getSupportFragmentManager();

        getOffer();
        createOfferInfoText();
        createCounter();
        createDoneButton();
        createBanner();
    }

    private void getOffer() {
        Intent intent = getIntent();
        currentOffer = (Offer)intent.getExtras().get("currentOffer");
        offerLifeTimeMin = intent.getExtras().getLong("offerLifeTimeMin");
    }

    private void createOfferInfoText() {
        offerInfoFromTextView = findViewById(R.id.offerInfoFromId);
        offerInfoFromTextView.setText(currentOffer.getFromLabelName());
        offerInfoToTextView = findViewById(R.id.offerInfoToId);
        offerInfoToTextView.setText(currentOffer.getToLabelName());
    }

    private void createCounter() {
        offerCounterTextView = findViewById(R.id.offerCounterTimeId);

        countDownTimer = new CountDownTimer(offerLifeTimeMin * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                offerCounterTextView.setText(String.format(Locale.getDefault(),
                        "%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                offerCounterTimeSec--;
            }
            @Override
            public void onFinish() {
                try {
                    finishOrder(true);
                } catch (JSONException e) {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, context,
                            "Invalid JSON Object: " + e);
                }
            }
        }.start();
    }

    private void createDoneButton() {
        doneButton = findViewById(R.id.offerDoneButtonId);
        doneButton.setOnClickListener(v -> {
            try {
                finishOrder(false);
            } catch (JSONException e) {
                DialogHelper.getInstance().showDialog(
                        DialogHelper.DialogType.ERROR, context,
                        "Invalid JSON Object: " + e);
            }
        });
    }

    private void finishOrder(boolean autoClosed) throws JSONException {
        countDownTimer.cancel();
        currentOffer.setAutoClosed(autoClosed);
        restRequestQueue.addPostJsonObjectRequest(
                URIHelper.getInstance().finishOfferEndPoint(),
                JSONObjectBuilder.offerToJson(currentOffer),
                result -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, context, "finishOrder: ", error);
                }
        );
    }

    private void createBanner() {
        ImageView imageView = findViewById(R.id.bannerId);
        restRequestQueue.addGetImageRequest(
                URIHelper.getInstance().bannerEndPoint(),
                imageView::setImageBitmap,
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, context, "createBanner: ", error);
                }
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
