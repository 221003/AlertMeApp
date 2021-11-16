package com.example.alertmeapp.api.serverResponse;

import com.example.alertmeapp.api.serverRequest.AlertBody;
import com.google.gson.annotations.SerializedName;

public class AlertResponse {
    @SerializedName("data")
    private AlertBody alert;

    public AlertResponse(AlertBody alert) {
        this.alert = alert;
    }

    public AlertBody getAlert() {
        return alert;
    }

    public void setAllAlert(AlertBody alert) {
        this.alert = alert;
    }
}
