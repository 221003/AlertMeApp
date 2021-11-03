package com.example.alertmeapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AlertBody {
    private Long userId;
    private Long alertTypeId;
    private String title;
    private String description;
    private int number_of_votes;
    @SerializedName("coordinate_x")
    private int latitude;
    @SerializedName("coordinate_y")
    private int longitude;
    private String expire_date;

    public AlertBody(Long userId, Long alertTypeId, String title, String description, int number_of_votes, int latitude, int longitude, String expire_date) {
        this.userId = userId;
        this.alertTypeId = alertTypeId;
        this.title = title;
        this.description = description;
        this.number_of_votes = number_of_votes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expire_date = expire_date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAlertTypeId() {
        return alertTypeId;
    }

    public void setAlertTypeId(Long alertTypeId) {
        this.alertTypeId = alertTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumber_of_votes() {
        return number_of_votes;
    }

    public void setNumber_of_votes(int number_of_votes) {
        this.number_of_votes = number_of_votes;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    @Override
    public String toString() {
        return "AlertBody{" +
                "userId=" + userId +
                ", alertTypeId=" + alertTypeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_votes=" + number_of_votes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", expire_date='" + expire_date + '\'' +
                '}';
    }
}
