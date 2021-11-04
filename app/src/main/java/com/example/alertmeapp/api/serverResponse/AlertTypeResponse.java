package com.example.alertmeapp.api.serverResponse;

import com.example.alertmeapp.api.serverRequest.AlertType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlertTypeResponse {
    @SerializedName("data")
    private List<AlertType> allAlertTypes;

    public AlertTypeResponse(List<AlertType> allAlertTypes) {
        this.allAlertTypes = allAlertTypes;
    }

    public List<AlertType> getAllAlertTypes() {
        return allAlertTypes;
    }

    public void setAllAlertTypes(List<AlertType> allAlertTypes) {
        this.allAlertTypes = allAlertTypes;
    }
}
