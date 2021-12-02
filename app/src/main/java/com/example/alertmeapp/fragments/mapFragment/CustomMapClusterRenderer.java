package com.example.alertmeapp.fragments.mapFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomMapClusterRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {

    private final float HUE_CARMINE = 345;
    private Context context;

    CustomMapClusterRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
        return cluster.getSize() >= 2;
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<T> cluster, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitMap());
        markerOptions.icon(bitmap);

    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<T> cluster, @NonNull Marker marker) {
        super.onClusterUpdated(cluster, marker);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitMap());
        marker.setIcon(bitmap);
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

    private Bitmap getBitMap() {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_twotone_notifications_active);
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0,
                drawable.getIntrinsicWidth() / 2,
                drawable.getIntrinsicHeight() / 2
        );
        Bitmap bitmap = Bitmap.createBitmap(width / 2, height / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }
}
