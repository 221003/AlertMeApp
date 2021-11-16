package com.example.alertmeapp.api.serverRequest;

import java.util.Date;

public class AlertFullBody {
    private Long id;
    private User user;
    private Date expire_date;
    private AlertType alertType;
    private String title;
    private String description;
    private int number_of_votes;
    private double latitude;
    private double longitude;
    private String image;

    public AlertFullBody(Long id, User user, Date expire_date, AlertType alertType, String title, String description, int number_of_votes, double latitude, double longitude, String image) {
        this.id = id;
        this.user = user;
        this.expire_date = expire_date;
        this.alertType = alertType;
        this.title = title;
        this.description = description;
        this.number_of_votes = number_of_votes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Date getExpire_date() {
        return expire_date;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNumber_of_votes() {
        return number_of_votes;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpire_date(Date expire_date) {
        this.expire_date = expire_date;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumber_of_votes(int number_of_votes) {
        this.number_of_votes = number_of_votes;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AlertFullBody{" +
                "id=" + id +
                ", user=" + user +
                ", expire_date=" + expire_date +
                ", alertType=" + alertType +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_votes=" + number_of_votes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", image='" + image + '\'' +
                '}';
    }
}
