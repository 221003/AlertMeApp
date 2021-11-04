package com.example.alertmeapp.api.serverRequest;

import com.google.gson.annotations.SerializedName;

public class User {

    private Integer id;
    @SerializedName("first_name")
    private String fistName;
    @SerializedName("last_name")
    private String lastName;

    public User(Integer id, String fistName, String lastName) {
        this.id = id;
        this.fistName = fistName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
