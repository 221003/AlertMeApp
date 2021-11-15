package com.example.alertmeapp.utils;

import android.location.Location;

import com.example.alertmeapp.fragments.alert.list.AlertItem;

import java.util.Comparator;

public class DistanceComparator implements Comparator<AlertItem> {

    @Override
    public int compare(AlertItem item1, AlertItem item2) {
        LoggedInUser user = LoggedInUser.getInstance(null, null, null);
        Location userLocation = new Location("user");
        userLocation.setLatitude(user.getLastLatitude());
        userLocation.setLongitude(user.getLastLongitude());

        Location item1Location = new Location("item1");
        item1Location.setLatitude(item1.getAlert().getLatitude());
        item1Location.setLongitude(item1.getAlert().getLongitude());

        Location item2Location = new Location("item2");
        item2Location.setLatitude(item2.getAlert().getLatitude());
        item2Location.setLongitude(item2.getAlert().getLongitude());

        return (int) (userLocation.distanceTo(item1Location) - userLocation.distanceTo(item2Location));
    }
}
