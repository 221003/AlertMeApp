package com.example.alertmeapp.api.requests;

import com.example.alertmeapp.api.data.Alert;

public class AlertRequest extends Alert {

    private Long userId;
    private Long alertTypeId;

    public AlertRequest(String title, String description, int number_of_votes,
                        double latitude, double longitude, Long userId, Long alertTypeId) {
        super(title, description, number_of_votes, latitude, longitude);
        this.userId = userId;
        this.alertTypeId = alertTypeId;
    }

    public AlertRequest() {
    }

    public static class Builder {
        private String title;
        private String description;
        private int number_of_votes;
        private double latitude;
        private double longitude;
        private Long userId;
        private Long alertTypeId;
        private String image;


        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withNumberOfVotes(int number_of_votes) {
            this.number_of_votes = number_of_votes;
            return this;
        }

        public Builder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder withUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder withAlertTypeId(Long alertTypeId) {
            this.alertTypeId = alertTypeId;
            return this;
        }

        public Builder withImage(String image) {
            this.image = image;
            return this;
        }

        public AlertRequest build() {
            AlertRequest alertRequest = new AlertRequest();
            alertRequest.description = this.description;
            alertRequest.title = this.title;
            alertRequest.latitude = this.latitude;
            alertRequest.longitude = this.longitude;
            alertRequest.number_of_votes = this.number_of_votes;
            alertRequest.alertTypeId = this.alertTypeId;
            alertRequest.userId = this.userId;
            alertRequest.image = this.image;
            return alertRequest;
        }
    }


    @Override
    public String toString() {
        return "AlertRequest{" +
                "userId=" + userId +
                ", alertTypeId=" + alertTypeId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", number_of_votes=" + number_of_votes +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", expire_date='" + expire_date + '\'' +
                '}';
    }
}
