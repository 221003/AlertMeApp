package com.example.alertmeapp.api.serverResponse;

import com.example.alertmeapp.api.serverRequest.User;
import com.google.gson.annotations.SerializedName;

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
