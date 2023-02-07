/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cotraveler.androidapp.BuildConfig;
import com.cotraveler.androidapp.rest.RestRequestQueue;
import com.cotraveler.androidapp.utils.DialogHelper;
import com.cotraveler.androidapp.utils.JSONObjectBuilder;
import com.cotraveler.androidapp.utils.LocaleHelper;
import com.cotraveler.androidapp.utils.MapHelper;
import com.cotraveler.androidapp.utils.URIHelper;
import com.cotraveler.androidapp.R;
import com.google.android.gms.maps.MapsInitializer;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RestRequestQueue restRequestQueue;

    Spinner languageSpinner;
    Button driverButton, passengerButton, exitButton;
    TextView versionNameTextView;

    Locale myLocale;
    String currentLang;

    int currentVersionCode, newVersionCode;
    String currentVersionName, newVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapsInitializer.initialize(getApplicationContext());

        restRequestQueue = new RestRequestQueue(this);

        createLanguageSpinner();
        createButtons();
        MapHelper.getInstance().getLabels(restRequestQueue, this);
        initVersionValues();
    }

    private void initVersionValues() {
        currentVersionCode = BuildConfig.VERSION_CODE;
        currentVersionName = BuildConfig.VERSION_NAME;
        showVersionName();
        restRequestQueue.addGetJsonObjectRequest(
                URIHelper.getInstance().initVersionValuesEndPoint(),
                result -> {
                    try {
                        newVersionCode = Integer.parseInt(result.get("versionCode").toString());
                        newVersionName = result.get("versionName").toString();
                        checkVersion();
                    } catch (JSONException e) {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.ERROR, this,
                                "Invalid JSON Object: " + e);
                    }
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, this, "initVersionValues: ", error);
                }
        );
    }

    private void showVersionName() {
        versionNameTextView = findViewById(R.id.versionNameTextId);
        versionNameTextView.setText(getString(R.string.mainVersionNameText, currentVersionName));
    }

    private void checkVersion() {
        if ((currentVersionCode != newVersionCode) ||
                (!currentVersionName.equals(newVersionName))) {
            DialogHelper.getInstance().showDialog(
                    DialogHelper.DialogType.WARNING, this,
                    R.string.warningUpdateIsAvailableText);
        }
    }

    private void createLanguageSpinner() {
        languageSpinner = findViewById(R.id.languageSpinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.component_spinner_language_list,
                Arrays.asList("SELECT LANGUAGE", "EN", "KZ", "RU"));
        adapter.setDropDownViewResource(R.layout.component_spinner_language_list);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("kk");
                        break;
                    case 3:
                        setLocale("ru");
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void createButtons() {
        driverButton = findViewById(R.id.buttonDriverId);
        driverButton.setOnClickListener(v -> {
            if (MapHelper.getInstance().mapIsReady(this)) {
                Intent intent = new Intent(this, DriverActivity.class);
                startActivity(intent);
            }
        });
        passengerButton = findViewById(R.id.buttonPassengerId);
        passengerButton.setOnClickListener(v -> {
            if (MapHelper.getInstance().mapIsReady(this)) {
                checkPossibilityPassenger();
            }
        });
    }

    private void checkPossibilityPassenger() {
        @SuppressLint("HardwareIds")
        String clientId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        restRequestQueue.addPostJsonObjectRequest(
                URIHelper.getInstance().possibilityPassengerEndPoint(),
                new JSONObjectBuilder().addKeyValue("clientId", clientId).buildJSONObject(),
                result -> {
                    try {
                        if (result != null &&
                                Boolean.parseBoolean(result.get("possibilityPassenger").toString())) {
                            startPassengerActivity();
                        } else {
                            DialogHelper.getInstance().showDialog(
                                    DialogHelper.DialogType.WARNING, this,
                                    R.string.warningPossibilityPassengerText);
                        }
                    } catch (JSONException e) {
                        DialogHelper.getInstance().showDialog(
                                DialogHelper.DialogType.ERROR, this,
                                "Invalid JSON Object: " + e);
                    }
                },
                error -> {
                    DialogHelper.getInstance().showDialog(
                            DialogHelper.DialogType.ERROR, this, "checkPossibilityPassenger: ", error);
                }
        );
    }

    private void startPassengerActivity() {
        Intent intent = new Intent(this, PassengerActivity.class);
        startActivity(intent);
    }

    public void setLocale(String localeName) {
        Context context = LocaleHelper.setLocale(this, localeName);
        myLocale = new Locale(localeName);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.putExtra(currentLang, localeName);
        startActivity(refresh);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}