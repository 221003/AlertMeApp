package com.example.alertmeapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.RestAdapter;
import com.example.alertmeapp.api.serverRequest.AlertFullBody;
import com.example.alertmeapp.api.serverResponse.AlertResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertDetailsFragment extends Fragment {
    public AlertDetailsFragment() {}

    public static AlertDetailsFragment newInstance(long alertId) {
        AlertDetailsFragment f = new AlertDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("alertId", alertId);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = getView().findViewById(R.id.details_close);
        imageView.setOnClickListener((event) -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        });

        Bundle args = getArguments();
        long alertId = args.getLong("alertId");
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<AlertResponse> alert = service.getAlert(alertId);
        alert.enqueue(new Callback<AlertResponse>() {
            @Override
            public void onResponse(Call<AlertResponse> call, Response<AlertResponse> response) {
                if (response.isSuccessful()) {
                    populateFragmentView(response.body().getAlertFullBody());
                } else {
                    System.out.println("Unsuccessful to fetch alert");
                }
            }

            @Override
            public void onFailure(Call<AlertResponse> call, Throwable t) {
                System.out.println("Failed to fetch alert");
            }
        });
    }

    private void populateFragmentView(AlertFullBody alert) {
        ImageView imageView = getView().findViewById(R.id.details_image);
        TextView titleView = getView().findViewById(R.id.details_title);
        TextView alertTypeView = getView().findViewById(R.id.details_alert_type);
        TextView authorView = getView().findViewById(R.id.details_author);
        TextView locationView = getView().findViewById(R.id.details_location);

        imageView.setImageBitmap(getImageBitmap(alert.getImage()));

        titleView.setText(alert.getTitle());

        String alertName = alert.getAlertType().getName();
        alertName = alertName.substring(0, 1).toUpperCase() + alertName.substring(1).toLowerCase();
        alertTypeView.setText(alertName);

        String author = alert.getUser().getFistName() + " " + alert.getUser().getLastName();
        authorView.setText(author);

        locationView.setText(getAlertAddress(alert.getLatitude(), alert.getLongitude()));
    }

    private Bitmap getImageBitmap(String image) {
        byte[] decodedImage = Base64.getDecoder().decode(image);
        return BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
    }

    private String getAlertAddress(Double latitude, Double longitude) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String[] splitAddress = address.split(",");

        return splitAddress[0] + ", " + splitAddress[1];
    }
}