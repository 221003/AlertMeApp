package com.example.alertmeapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.RestAdapter;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {

    private static final int TRANSLATION_Y = 1100;
    private static String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int REQUEST_LOCATION_CODE = 103;
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private final Map<Marker, JsonElement> markersAssociatedWithAlerts = new HashMap<>();
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
            LinearLayout details = getView().findViewById(R.id.maps_details);
            details.setOnClickListener(view -> details.animate().translationYBy(TRANSLATION_Y));

            map = googleMap;
            getLastLocation();
            getAlerts();
            googleMap.setOnMarkerClickListener(marker -> {
                marker.showInfoWindow();
                Optional<JsonElement> alert = markersAssociatedWithAlerts.entrySet().stream()
                        .filter(e -> e.getKey().equals(marker))
                        .map(Map.Entry::getValue)
                        .findFirst();

                details.animate().translationYBy(-TRANSLATION_Y);

                return true;
            });
        }
    };

    private BitmapDescriptor getColorMarker(String alertType){
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


    private void createMarkers(JsonArray data) {
        for(JsonElement alert : data){
            double latitude = alert.getAsJsonObject().get("latitude").getAsDouble();
            double longitude = alert.getAsJsonObject().get("longitude").getAsDouble();
            String title = alert.getAsJsonObject().get("title").getAsString();
            String alertType = alert.getAsJsonObject().get("alertType")
                    .getAsJsonObject().get("name").getAsString();
            LatLng latlng = new LatLng(latitude, longitude);
            System.out.println(alertType);
            BitmapDescriptor bitmap = getColorMarker(alertType);
            Marker marker = map.addMarker(
                    new MarkerOptions().position(latlng).icon(bitmap).title(title)
            );
            markersAssociatedWithAlerts.put(marker, alert);
        }
    }
    private JsonArray getParsedDataFrom(Response<ResponseBody> response) throws IOException {
        String json = response.body().string();
        JsonParser jsonParser = new JsonParser();
        JsonObject root = jsonParser.parse(json).getAsJsonObject();
        return root.getAsJsonArray("data");
    }

    private void getAlerts(){
        Call<ResponseBody> call = service.getAlerts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonArray data = getParsedDataFrom(response);
                    createMarkers(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    public void getLastLocation() {
        System.out.println("bylem");
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_LOCALIZATION, REQUEST_LOCATION_CODE);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    System.out.println("bylem1");
                                    System.out.println(location.getLatitude()+" "+location.getLongitude());
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));
                                } else {
                                    System.out.println("bylem2");

                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.759f, 19.457f), 11));
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("bylem3");

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.759f, 19.457f), 11));
            }
        });
    }
}