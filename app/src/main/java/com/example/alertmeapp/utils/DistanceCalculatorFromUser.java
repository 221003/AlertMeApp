package com.example.alertmeapp.utils;

import android.location.Location;

public class DistanceCalculatorFromUser {


    public static final int ONE_KILOMETER = 1000;

    public static String count(double longitude, double latitude) {
        LoggedInUser user = LoggedInUser.getInstance(null, null, null);

        Location userLocation = new Location("point A");
        userLocation.setLatitude(user.getLastLatitude());
        userLocation.setLongitude(user.getLastLongitude());

        Location alertLocation = new Location("point B");
        alertLocation.setLatitude(latitude);
        alertLocation.setLongitude(longitude);

        float distance = userLocation.distanceTo(alertLocation);
        String res;
        if (distance > ONE_KILOMETER) {
            res = Math.round(distance / ONE_KILOMETER) + " km";
        } else {
            res = Math.round(distance) + " m";
        }
        return res;
    }
}
