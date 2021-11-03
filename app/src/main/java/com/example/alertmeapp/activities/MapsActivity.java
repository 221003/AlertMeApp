package com.example.alertmeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.alertmeapp.R;
import com.example.alertmeapp.fragments.AlertFormFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private Marker alertLocalization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cord = getCordsFromBundle();

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cord));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cord, 10));
        Marker marker = googleMap.addMarker(new MarkerOptions().position(cord).draggable(true));
        alertLocalization = marker;

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                alertLocalization = marker;
            }
        });
    }

    private LatLng getCordsFromBundle() {
        Bundle extras = getIntent().getExtras();
        Double longitude = 51.759;
        Double latitude = 19.457;
        if (extras != null) {
            longitude = extras.getDouble("longitude");
            latitude = extras.getDouble("latitude");
        }
        return new LatLng(latitude, longitude);
    }


    public void onClickCords(View view) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("longitude",Float.valueOf(String.valueOf(alertLocalization.getPosition().longitude)));
        editor.putFloat("latitude",Float.valueOf(String.valueOf(alertLocalization.getPosition().latitude)));
        editor.apply();

        finish();
    }
}
