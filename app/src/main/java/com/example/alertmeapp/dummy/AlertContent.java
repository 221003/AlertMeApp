package com.example.alertmeapp.dummy;

import android.location.Location;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.serverRequest.AlertBody;
import com.example.alertmeapp.api.RestAdapter;
import com.example.alertmeapp.api.serverResponse.AllAlertsResponse;
import com.example.alertmeapp.logedInUser.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertContent {

    private List<AlertItem> alertItems = new ArrayList<AlertItem>();
    private RecyclerView recyclerView;

    public AlertContent(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        getAlerts();
    }

    private void getAlerts() {
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<AllAlertsResponse> allAlerts = service.getAllAlerts();
        allAlerts.enqueue(new Callback<AllAlertsResponse>() {
            @Override
            public void onResponse(Call<AllAlertsResponse> call, Response<AllAlertsResponse> response) {
                if (response.isSuccessful()) {
                    List<AlertBody> allAlert = response.body().getAllAlert();
                    allAlert.stream().forEach(alert -> {
                        alertItems.add(new AlertItem(alert.getAlertType().getName(), alert.getTitle(), countDistance(alert.getLongitude(), alert.getLatitude())));
                    });
                    recyclerView.setAdapter(new MyListRecyclerViewAdapter(alertItems));
                } else {
                    System.out.println("Unsuccessful to fetch all alerts AlertContent.class");
                }
            }

            @Override
            public void onFailure(Call<AllAlertsResponse> call, Throwable t) {
                System.out.println("2Failed to fetch all alerts AlertContent.class");
            }
        });
    }

    private String countDistance(double longitude, double latitude) {
        LoggedInUser user = LoggedInUser.getInstance(null, null, null);

        Location userLocation = new Location("point A");
        userLocation.setLatitude(Float.valueOf(user.getLastLatitude()));
        userLocation.setLongitude(Float.valueOf(user.getLastLongitude()));

        Location alertLocation = new Location("point B");
        alertLocation.setLatitude(latitude);
        alertLocation.setLongitude(longitude);

        float distance = userLocation.distanceTo(alertLocation);
        String res = String.valueOf(distance);
        if (distance > 1000) {
            res = String.valueOf(Math.round(distance / 1000)) + " km";
        } else {
            res = String.valueOf(Math.round(distance)) + " m";
        }
        return res;
    }

    public static class AlertItem {
        public final String alertType;
        public final String title;
        public final String distance;

        public AlertItem(String alertType, String title, String distance) {
            this.alertType = alertType;
            this.title = title;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "AlertItem{" +
                    "alertType='" + alertType + '\'' +
                    ", title='" + title + '\'' +
                    ", distance='" + distance + '\'' +
                    '}';
        }
    }
}