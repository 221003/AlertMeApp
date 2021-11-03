package com.example.alertmeapp.logedInUser;

import com.example.alertmeapp.api.User;

public class LoggedInUser {
    private static LoggedInUser loggedUser;

    private Integer id;
    private String fistName;
    private String lastName;

    private LoggedInUser(User user) {
        this.id = user.getId();
        this.fistName = user.getFistName();
        this.lastName = user.getLastName();
    }

    public static LoggedInUser getInstance(User user) {
        if (loggedUser == null) {
            loggedUser = new LoggedInUser(user);
        }
        return loggedUser;
    }


    public Integer getId() {
        return id;
    }

    public String getFistName() {
        return fistName;
    }

    public String getLastName() {
        return lastName;
    }
}
