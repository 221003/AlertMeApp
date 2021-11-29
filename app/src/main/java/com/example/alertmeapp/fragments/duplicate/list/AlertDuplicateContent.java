package com.example.alertmeapp.fragments.duplicate.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;

import com.example.alertmeapp.fragments.alert.AlertItem;
import com.example.alertmeapp.utils.DistanceComparator;
import com.example.alertmeapp.utils.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertDuplicateContent {
    private DuplicateViewAdapter adapter;
    private final RecyclerView recyclerView;
    private final List<AlertItem> items;

    private final Integer RANGE_OF_DUPLICATE_ALERTS_IN_METERS = 12000;
    private final Double latitude;
    private final Double longitude;
    private final String alertName;
    private final FragmentActivity fragmentActivity;

    public AlertDuplicateContent(RecyclerView recyclerView, DuplicateViewAdapter adapter, List<AlertItem> items,
                                 String alertName, Double longitude, Double latitude, FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.longitude = longitude;
        this.latitude = latitude;
        this.alertName = alertName;
        this.adapter = adapter;
        this.items = items;
        this.recyclerView = recyclerView;
        getAlertWithCategory(alertName);

    }

    private void getAlertWithCategory(String alertName) {
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<ResponseMultipleData<Alert>> allAlerts = service.getAlertByDistance(latitude, longitude, Double.valueOf(RANGE_OF_DUPLICATE_ALERTS_IN_METERS));
        allAlerts.enqueue(new Callback<ResponseMultipleData<Alert>>() {

            @Override
            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
                if (response.isSuccessful()) {
                    List<Alert> alerts = response.body().getData();
                    List<AlertItem> temp = new ArrayList<>();

                    alerts.stream()
                            .filter((alert -> alert.getAlertType().getName().equals(alertName)))
                            .forEach(alert -> temp.add(new AlertItem(alert, countDistance(alert.getLongitude(), alert.getLatitude()))));

                    if (items.size() != temp.size()) {
                        items.clear();
                        items.addAll(temp);
                    }
                    items.sort(new DistanceComparator());
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                    if (items.size() == 0) {
                        Context applicationContext = fragmentActivity.getApplicationContext();
                        SharedPreferences sharedPref = applicationContext.getSharedPreferences(
                                applicationContext.getString(R.string.shared_preferences), Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("createAlert", "yes");
                        editor.apply();
                        fragmentActivity.getSupportFragmentManager().popBackStack();
                    }
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
