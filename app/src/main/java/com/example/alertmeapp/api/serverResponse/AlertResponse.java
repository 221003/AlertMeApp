package com.example.alertmeapp.api.serverResponse;

import com.example.alertmeapp.api.serverRequest.AlertFullBody;
import com.google.gson.annotations.SerializedName;

public class AlertResponse {
    @SerializedName("data")
    private AlertFullBody alertFullBody;

    public AlertResponse(AlertFullBody alertFullBody) {
        this.alertFullBody = alertFullBody;
    }

    public AlertFullBody getAlertFullBody() {
        return alertFullBody;
    }

    public void setAlertFullBody(AlertFullBody alertFullBody) {
        this.alertFullBody = alertFullBody;
    }
}
