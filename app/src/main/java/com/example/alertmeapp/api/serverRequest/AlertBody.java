package com.example.alertmeapp.api.serverRequest;

public class AlertBody {

    private Long id;
    private Long userId;
    private Long alertTypeId;
    private String title;
    private String description;
    private int number_of_votes;
    private double latitude;
    private double longitude;
    private String expire_date;
    private AlertType alertType;

    private String image;

    public AlertBody(Long userId, Long alertTypeId, String title, String description, int number_of_votes, double latitude, double longitude, String expire_date, String image) {
        this.userId = userId;
        this.alertTypeId = alertTypeId;
        this.title = title;
        this.description = description;
        this.number_of_votes = number_of_votes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expire_date = expire_date;
        this.alertType = alertType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AlertBody{" +
                "id=" + id +
                ", userId=" + userId +
                ", alertTypeId=" + alertTypeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_votes=" + number_of_votes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", expire_date='" + expire_date + '\'' +
                ", image=" + image +
                '}';
    }

}
