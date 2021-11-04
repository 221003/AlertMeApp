package com.example.alertmeapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServeLogInResponse {
    @SerializedName("data")
    private User user;

    public ServeLogInResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
