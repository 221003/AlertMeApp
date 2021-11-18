package com.example.alertmeapp.api.requests;

import android.content.Context;

import com.example.alertmeapp.notifications.FirebaseMessageReceiver;

public class UserSignInRequest {
    private String login;
    private String password;
    private String token;

    public UserSignInRequest(String login, String password, String token) {
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserSignInRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}