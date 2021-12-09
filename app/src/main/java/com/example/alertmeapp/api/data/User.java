package com.example.alertmeapp.api.data;

import com.google.gson.annotations.SerializedName;

public class User {

    private Long id;
    @SerializedName("first_name")
    private String fistName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("turn_off_notifications")
    private boolean turnOffNotifications;

    public User(Long id, String fistName, String lastName, boolean turnOffNotifications) {
        this.id = id;
        this.fistName = fistName;
        this.lastName = lastName;
        this.turnOffNotifications = turnOffNotifications;
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

    public boolean isTurnOffNotifications() {
        return turnOffNotifications;
    }

    public void setTurnOffNotifications(boolean turnOffNotifications) {
        this.turnOffNotifications = turnOffNotifications;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fistName='" + fistName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
