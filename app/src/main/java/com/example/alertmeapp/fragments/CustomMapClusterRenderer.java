package com.example.alertmeapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.Map;

public class CustomMapClusterRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {

    private final float HUE_CARMINE = 345;

    CustomMapClusterRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        return cluster.getSize() >= 2;
    }

    @Override
    protected void onBeforeClusterItemRendered(T alert,
                                               MarkerOptions markerOptions) {
        Alert markerItem = (Alert) alert;
        BitmapDescriptor bitmap = getColorMarker(markerItem.getAlertType().getName());
        markerOptions.icon(bitmap).title(alert.getTitle());
    }

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
}
