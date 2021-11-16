package com.example.alertmeapp.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {

    private static String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int REQUEST_LOCATION_CODE = 103;
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private final Map<Marker, Alert> markersAssociatedWithAlerts = new HashMap<>();
    private GoogleMap map;
    private final float HUE_CARMINE = 345;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            LoggedInUser instance = LoggedInUser.getInstance(null, null, null);
            System.out.println("Od usera: "+instance.getLastLongitude()+" "+instance.getLastLatitude());
            LatLng latLng = new LatLng(instance.getLastLatitude(), instance.getLastLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            getAlerts();
            googleMap.setOnMarkerClickListener(marker -> {
                marker.showInfoWindow();
                Optional<Alert> alert = markersAssociatedWithAlerts.entrySet().stream()
                        .filter(e -> e.getKey().equals(marker))
                        .map(Map.Entry::getValue)
                        .findFirst();
                //TODO Display detail information about an alert
                return true;
            });
        }
    };

    private BitmapDescriptor getColorMarker(String alertType) {
        switch (alertType) {
            case "danger":
                return BitmapDescriptorFactory.defaultMarker(HUE_CARMINE);
            case "warning":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            case "information":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            case "curiosity":
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }
        return BitmapDescriptorFactory.defaultMarker();
    }


    private void createMarkers(List<Alert> alerts) {
        for (Alert alert : alerts) {
            LatLng latlng = new LatLng(alert.getLatitude(), alert.getLongitude());
            BitmapDescriptor bitmap = getColorMarker(alert.getAlertType().getName());
            Marker marker = map.addMarker(
                    new MarkerOptions().position(latlng).icon(bitmap).title(alert.getTitle())
            );
            markersAssociatedWithAlerts.put(marker, alert);
        }
    }

    private void getAlerts() {
        Call<ResponseMultipleData<Alert>> call = service.getAllAlerts();
        call.enqueue(new Callback<ResponseMultipleData<Alert>>() {
            @Override
            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
                List<Alert> alerts = response.body().getData();
                createMarkers(alerts);
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<Alert>> call, Throwable t) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}