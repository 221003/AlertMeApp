package com.example.alertmeapp.utils;

import com.example.alertmeapp.api.data.User;

public class LoggedInUser {
    private static LoggedInUser loggedUser;

    private Long id;
    private String fistName;
    private String lastName;
    private Double lastLongitude;
    private Double lastLatitude;

    private LoggedInUser(User user, Double longitude, Double latitude) {
        this.id = user.getId();
        this.fistName = user.getFistName();
        this.lastName = user.getLastName();
        this.lastLongitude = longitude;
        this.lastLatitude = latitude;
    }

    public static LoggedInUser getInstance(User user, Double longitude, Double latitude) {
        if (loggedUser == null) {
            loggedUser = new LoggedInUser(user, longitude, latitude);
        }
        return loggedUser;
    }

    public static LoggedInUser getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(LoggedInUser loggedUser) {
        LoggedInUser.loggedUser = loggedUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(Double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public Double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }
}
