package com.example.alertmeapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertDetailsFragment extends Fragment {
    private static String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_CODE = 103;

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
        Alert alert = (Alert) args.getSerializable("alert");
        initDeleteButton(alert);
        populateFragmentView(alert);
    }

    private void initDeleteButton(Alert alert) {
        ImageView deleteButton = getView().findViewById(R.id.delete_button);
        if (alert.getUser().getId() != LoggedInUser.getInstance(null, null, null).getId()) {
            deleteButton.setVisibility(View.INVISIBLE);
        } else {
            deleteButton.setOnClickListener((click) -> {
                AlertMeService service = RestAdapter.getAlertMeService();
                Call<ResponseBody> deleteRequest = service.deleteAlert(Long.valueOf(alert.getId()));
                deleteRequest.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            displayToast("Alert deleted");
                            NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
                            navController.navigate(R.id.mapsFragment);
                        } else {
                            displayToast("Unsuccessful to delete alert");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        displayToast("Failed to delete alert");
                    }
                });
            });
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }

    private void populateFragmentView(Alert alert) {
        ImageView imageView = getView().findViewById(R.id.details_image);
        TextView titleView = getView().findViewById(R.id.details_title);
        TextView alertTypeView = getView().findViewById(R.id.details_alert_type);
        TextView authorView = getView().findViewById(R.id.details_author);
        TextView locationView = getView().findViewById(R.id.details_location);
        TextView numberOfVotesView = getView().findViewById(R.id.details_votes);
        TextView descriptionView = getView().findViewById(R.id.details_description);

        if(alert.getImage()!=null)
            imageView.setImageBitmap(getImageBitmap(alert.getImage()));

        titleView.setText(alert.getTitle());

        String alertName = alert.getAlertType().getName();
        alertName = alertName.substring(0, 1).toUpperCase() + alertName.substring(1).toLowerCase();
        alertTypeView.setText(alertName);

        String author = alert.getUser().getFistName() + " " + alert.getUser().getLastName();
        authorView.setText(author);

        locationView.setText(getAlertAddress(alert.getLatitude(), alert.getLongitude()));

        setDistanceTo(alert.getLatitude(), alert.getLongitude());

        numberOfVotesView.setText(String.valueOf(alert.getNumber_of_votes()));

        descriptionView.setText(alert.getDescription());
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

    private void setDistanceTo(Double latitude, Double longitude) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_LOCALIZATION, REQUEST_LOCATION_CODE);
        }

        TextView distanceToView = getView().findViewById(R.id.details_distance_to);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                            if (location != null) {
                                Location current = new Location(location);
                                current.setLatitude(latitude);
                                current.setLongitude(longitude);
                                int distance = (int) location.distanceTo(current);
                                distanceToView.setText(String.valueOf(distance) + " m");
                            } else {
                                distanceToView.setText("Unknown");
                            }
                        }
                ).addOnFailureListener(e -> distanceToView.setText("Unknown"));
    }
}