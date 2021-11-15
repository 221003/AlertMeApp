package com.example.alertmeapp.loggedInUser;

import com.example.alertmeapp.api.data.User;

public class LoggedInUser {
    private static LoggedInUser loggedUser;

    private Long id;
    private String fistName;
    private String lastName;
    private String lastLongitude;
    private String lastLatitude;

    private LoggedInUser(User user, String longitude, String latitude) {
        this.id = user.getId();
        this.fistName = user.getFistName();
        this.lastName = user.getLastName();
        this.lastLongitude = longitude;
        this.lastLatitude = latitude;
    }

    public static LoggedInUser getInstance(User user, String longitude, String latitude) {
        if (loggedUser == null) {
            loggedUser = new LoggedInUser(user, longitude, latitude);
        }
        return loggedUser;
    }


    public Long getId() {
        return id;
    }

    public String getFistName() {
        return fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(String lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public String getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(String lastLatitude) {
        this.lastLatitude = lastLatitude;
    }
}
