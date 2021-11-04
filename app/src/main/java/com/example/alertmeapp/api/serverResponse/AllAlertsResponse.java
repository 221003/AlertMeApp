package com.example.alertmeapp.api.serverResponse;

import com.example.alertmeapp.api.serverRequest.AlertBody;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllAlertsResponse {
    @SerializedName("data")
    private List<AlertBody> allAlert;

    public AllAlertsResponse(List<AlertBody> allAlertTypes) {
        this.allAlert = allAlertTypes;
    }

    public List<AlertBody> getAllAlert() {
        return allAlert;
    }

    public void setAllAlert(List<AlertBody> allAlert) {
        this.allAlert = allAlert;
    }
}
