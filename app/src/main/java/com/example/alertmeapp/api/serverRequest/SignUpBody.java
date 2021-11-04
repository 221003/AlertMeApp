package com.example.alertmeapp.api.serverRequest;

import com.google.gson.annotations.SerializedName;

public class SignUpBody {
    private String email;
    @SerializedName("first_name")
    private String fistName;
    @SerializedName("last_name")
    private String lastName;
    private String login;
    @SerializedName("password_hash")
    private String password;

    public SignUpBody(String email, String fistName, String lastName, String login, String password) {
        this.email = email;
        this.fistName = fistName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
