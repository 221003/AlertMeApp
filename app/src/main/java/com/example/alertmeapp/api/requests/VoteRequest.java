package com.example.alertmeapp.api.requests;

import com.google.gson.annotations.SerializedName;

public class VoteRequest {

    @SerializedName("alert_id")
    private Long alertId;
    @SerializedName("user_id")
    private Long userId;
    @SerializedName("is_upped")
    private boolean isUpped;

    public VoteRequest(Long alertId, Long userId, boolean isUpped) {
        this.alertId = alertId;
        this.userId = userId;
        this.isUpped = isUpped;
    }
    public VoteRequest(Long alertId, Long userId) {
        this.alertId = alertId;
        this.userId = userId;
    }


    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isUpped() {
        return isUpped;
    }

    public void setUpped(boolean upped) {
        isUpped = upped;
    }

    @Override
    public String toString() {
        return "VoteRequest{" +
                "alertId=" + alertId +
                ", userId=" + userId +
                ", isUpped=" + isUpped +
                '}';
    }
}
