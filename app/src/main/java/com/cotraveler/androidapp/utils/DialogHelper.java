/*
 * Copyright (c)  Aleksey Plekhanov 07.02.2023, 4:31
 */

package com.cotraveler.androidapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.ClientError;
import com.cotraveler.androidapp.R;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class DialogHelper {
    private static DialogHelper single_instance = null;

    public enum DialogType {
        WARNING, ERROR
    }

    Dialog dialog;

    private DialogHelper() {

    }

    public static DialogHelper getInstance() {
        if (single_instance == null)
            single_instance = new DialogHelper();

        return single_instance;
    }

    public void showDialog(DialogType dialogType, Context context, Integer textResId) {
        showDialog(dialogType, context, textResId, null);
    }

    public void showDialog(DialogType dialogType, Context context, String errorText) {
        showDialog(dialogType, context, null, errorText);
    }

    public void showDialog(DialogType dialogType, Context context, String errorInfo, Exception error) {
        if (error instanceof ClientError &&
                ((ClientError) error).networkResponse.statusCode == 404) {
            String body = new String(((ClientError) error).networkResponse.data, StandardCharsets.UTF_8);
            try {
                JSONObject errorJson = new JSONObject(body);
                showDialog(dialogType, context, null, errorJson.getString("message"));
            } catch (JSONException e) {
                showDialog(dialogType, context, null, "error on processed Custom Exception: " + e);
            }
        } else {
            showDialog(dialogType, context, null, errorInfo + ExceptionUtils.getStackTrace(error));
        }
    }

    private void showDialog(DialogType dialogType, Context context, Integer textResId, String errorText) {
        dialog = new Dialog(context, R.style.warningDialogStyle);
        dialog.setContentView(R.layout.component_alert_dialog);
        dialog.setCancelable(true);

        TextView text = dialog.findViewById(R.id.alertDialogTextId);
        switch (dialogType) {
            case WARNING:
                dialog.setTitle(R.string.warningDialogTitle);
                text.setText(textResId);
                break;
            case ERROR:
                dialog.setTitle(R.string.errorDialogTitle);
                text.setText(errorText);
                break;
        }

        Button button = dialog.findViewById(R.id.alertDialogOkButtonId);
        button.setOnClickListener(v -> dialog.cancel());

        dialog.show();
    }

}
