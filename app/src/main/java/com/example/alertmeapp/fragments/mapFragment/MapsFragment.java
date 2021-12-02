package com.example.alertmeapp.fragments.mapFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private GoogleMap map;
    private ClusterManager<Alert> clusterManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.clear();
            LoggedInUser instance = LoggedInUser.getInstance(null, null, null);
            LatLng latLng = new LatLng(instance.getLastLatitude(), instance.getLastLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            setUpClusterer();
            googleMap.setOnMarkerClickListener(clusterManager);
            getAlerts();
        }
    };

    private void setUpClusterer() {
        clusterManager = new ClusterManager<>(getContext(), map);
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        clusterManager.setRenderer(new CustomMapClusterRenderer<>(getContext(), map, clusterManager));
        clusterManager.setOnClusterItemClickListener(marker -> {
            Bundle bundle = new Bundle();
            bundle.putLong("alertId", marker.getId());
            NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
            navController.navigate(R.id.alertDetailsFragment, bundle);
            return false;
        });
    }

    private void addMarkers(List<Alert> alerts) {
        for (Alert alert : alerts) {
            clusterManager.addItem(alert);
        }
        clusterManager.cluster();
    }

    private void getAlerts() {
        Call<ResponseMultipleData<Alert>> call = service.getAllAlerts();
        call.enqueue(new Callback<ResponseMultipleData<Alert>>() {
            @Override
            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
                List<Alert> alerts = response.body().getData();
                addMarkers(alerts);
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