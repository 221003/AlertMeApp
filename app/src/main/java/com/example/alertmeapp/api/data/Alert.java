package com.example.alertmeapp.api.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

public class Alert implements ClusterItem, Serializable {

    protected Long id;
    protected String title;
    protected String description;
    protected int number_of_votes;
    protected double latitude;
    protected double longitude;
    protected String expire_date;
    private AlertType alertType;
    private User user;
    protected String image;

    public Alert() {
    }

    public Alert(Long id, String title, String description, int number_of_votes, double latitude, double longitude, String expire_date, AlertType alertType, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.number_of_votes = number_of_votes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expire_date = expire_date;
        this.alertType = alertType;
        this.user = user;
    }

    public Alert(String title, String description, int number_of_votes, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.number_of_votes = number_of_votes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude,longitude);
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_votes=" + number_of_votes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", expire_date='" + expire_date + '\'' +
                ", alertType=" + alertType +
                ", user=" + user +
                ", image='" + image + '\'' +
                '}';
    }
}
