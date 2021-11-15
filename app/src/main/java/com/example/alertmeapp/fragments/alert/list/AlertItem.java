package com.example.alertmeapp.fragments.alert.list;

import com.example.alertmeapp.api.data.Alert;

public class AlertItem {
    public Alert alert;
    public String distance;

    public AlertItem(Alert alert, String distance) {
        this.alert = alert;
        this.distance = distance;
    }


    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}