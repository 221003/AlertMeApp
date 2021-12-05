package com.example.alertmeapp.fragments.alert;

import com.example.alertmeapp.api.data.Alert;

public class AlertItem {
    private Alert alert;
    private String distance;

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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AlertItem)) {
            return false;
        }
        return alert.getId().equals(((AlertItem)other).getAlert().getId());
    }

    @Override
    public int hashCode() {
        return alert.getId().hashCode();
    }

    @Override
    public String toString() {
        return "AlertItem{" +
                "alert=" + alert +
                ", distance='" + distance + '\'' +
                '}';
    }
}