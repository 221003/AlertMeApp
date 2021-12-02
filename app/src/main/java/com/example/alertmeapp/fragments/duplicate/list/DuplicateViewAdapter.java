package com.example.alertmeapp.fragments.duplicate.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.Vote;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.requests.VoteRequest;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;

import com.example.alertmeapp.fragments.alert.AlertItem;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DuplicateViewAdapter extends RecyclerView.Adapter<DuplicateViewAdapter.ViewHolder> {

    private final FragmentActivity activity;
    private final List<AlertItem> alertList;
    private final PorterDuffColorFilter GREEN = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter GRAY = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private final long USER_ID = 1L;
    private final int NO_DUPLICATE_VALUE = -1;
    private RadioButton lastRadioButton = null;
    private int lastPositionButton = 0;

    public DuplicateViewAdapter(FragmentActivity activity, List<AlertItem> items) {
        this.alertList = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DuplicateViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_duplicate, parent, false);
        return new DuplicateViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DuplicateViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Context applicationContext = activity.getApplicationContext();
        SharedPreferences sharedPref = applicationContext.getSharedPreferences(
                applicationContext.getString(R.string.shared_preferences), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("alertDuplicateId", String.valueOf(NO_DUPLICATE_VALUE));
        editor.apply();

        Alert alert = alertList.get(position).getAlert();
        String distance = alertList.get(position).getDistance();

        holder.titleView.setText(alert.getTitle());
        holder.typeView.setText(alert.getAlertType().getName());
        holder.rangeView.setText(distance);
        int color = getColorBasedOnAlertType(alert.getAlertType().getName());
        holder.materialCardView.setStrokeColor(color);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastRadioButton == null) {
                    lastRadioButton = (RadioButton) view;
                    lastPositionButton = position;
                } else if (lastPositionButton == position) {
                    lastRadioButton.setChecked(false);
                    lastRadioButton = (RadioButton) view;
                    lastPositionButton = NO_DUPLICATE_VALUE;
                } else {
                    lastRadioButton.setChecked(false);
                    lastRadioButton = (RadioButton) view;
                    lastPositionButton = position;
                }
                Context applicationContext = activity.getApplicationContext();
                SharedPreferences sharedPref = applicationContext.getSharedPreferences(
                        applicationContext.getString(R.string.shared_preferences), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("alertDuplicateId", String.valueOf(alertList.get(lastPositionButton).getAlert().getId()));
                editor.apply();
            }
        });

        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("alertId", alert.getId());
                NavController navController = Navigation.findNavController(activity, R.id.fragmentController);
                navController.navigate(R.id.alertDetailsFragment, bundle);
            }
        });
    }

    private int getColorBasedOnAlertType(String alertType) {
        int color = 0;
        switch (alertType) {
            case "danger":
                color = Color.parseColor("#960018"); //CARMINE
                break;
            case "warning":
                color = Color.parseColor("#FF8000"); //ORANGE
                break;
            case "information":
                color = Color.parseColor("#007FFF"); //AZURE
                break;
            case "curiosity":
                color = Color.GREEN;
                break;
        }
        return color;
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView typeView;
        private final TextView rangeView;
        private final RadioButton radioButton;
        private final MaterialCardView materialCardView;

        public ViewHolder(View view) {
            super(view);
            materialCardView = view.findViewById(R.id.material_card);
            titleView = view.findViewById(R.id.titleTextView);
            typeView = view.findViewById(R.id.typeTextView);
            rangeView = view.findViewById(R.id.rangeTextView);
            radioButton = view.findViewById(R.id.radio_button);
        }
    }
}
