package com.example.alertmeapp.fragments.alert.list;

import android.location.Location;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.utils.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertContent {

    private final List<AlertItem> alertItems = new ArrayList<AlertItem>();
    private final RecyclerView recyclerView;

    public AlertContent(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        getAlerts();
    }

    private void getAlerts() {
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<ResponseMultipleData<Alert>> allAlerts = service.getAllAlerts();
        allAlerts.enqueue(new Callback<ResponseMultipleData<Alert>>() {

            @Override
            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
                if (response.isSuccessful()) {
                    List<Alert> alerts = response.body().getData();
                    alerts.forEach(alert -> {
                        alertItems.add(new AlertItem(alert, countDistance(alert.getLongitude(), alert.getLatitude())));
                    });
                    recyclerView.setAdapter(new MyListRecyclerViewAdapter(alertItems));
                } else {
                    System.out.println("Unsuccessful to fetch all alerts AlertContent.class");
                }
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<Alert>> call, Throwable t) {
                System.out.println("Failed to fetch all alerts AlertContent.class");
            }
        });
    }

    private String countDistance(double longitude, double latitude) {
        LoggedInUser user = LoggedInUser.getInstance(null, null, null);
        Location userLocation = new Location("point A");
        userLocation.setLatitude(user.getLastLatitude());
        userLocation.setLongitude(user.getLastLongitude());

        Location alertLocation = new Location("point B");
        alertLocation.setLatitude(latitude);
        alertLocation.setLongitude(longitude);

        float distance = userLocation.distanceTo(alertLocation);
        String res;
        if (distance > 1000) {
            res = Math.round(distance / 1000) + " km";
        } else {
            res = Math.round(distance) + " m";
        }
        return res;
    }


}