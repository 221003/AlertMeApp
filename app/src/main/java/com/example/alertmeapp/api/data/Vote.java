package com.example.alertmeapp.api.data;

public class Vote {

    private Long id;
    private Alert alert;
    private User user;
    private boolean isUpped;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUpped() {
        return isUpped;
    }

    public void setUpped(boolean upped) {
        isUpped = upped;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", alert=" + alert +
                ", user=" + user +
                ", isUpped=" + isUpped +
                '}';
    }
}
