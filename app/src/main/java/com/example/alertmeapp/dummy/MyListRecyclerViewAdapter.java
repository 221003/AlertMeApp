package com.example.alertmeapp.dummy;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.dummy.AlertContent.AlertItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AlertItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<AlertItem> alertList;

    public MyListRecyclerViewAdapter(List<AlertItem> items) {
        alertList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleView.setText(alertList.get(position).title);
        holder.typeView.setText(alertList.get(position).alertType);
        holder.rangeView.setText(alertList.get(position).distance);
        int color = getColorBasedOnAlertType(alertList.get(position).alertType);
        holder.materialCardView.setStrokeColor(color);
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
        public final View mView;
        public final TextView titleView;
        public final TextView typeView;
        public final TextView rangeView;
        public final MaterialCardView materialCardView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            materialCardView = view.findViewById(R.id.material_card);
            titleView = (TextView) view.findViewById(R.id.titleTextView);
            typeView = (TextView) view.findViewById(R.id.typeTextView);
            rangeView = (TextView) view.findViewById(R.id.rangeTextView);
        }

    }
}