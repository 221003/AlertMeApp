package com.example.alertmeapp.fragments.alert.list;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<AlertItem> alertList;
    private final PorterDuffColorFilter GREEN = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter GRAY = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);


    public MyListRecyclerViewAdapter(List<AlertItem> items) {
        alertList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Alert alert = alertList.get(position).getAlert();
        String distance = alertList.get(position).getDistance();

        holder.titleView.setText(alert.getTitle());
        holder.typeView.setText(alert.getAlertType().getName());
        holder.rangeView.setText(distance);
        holder.alertVotes.setText(String.valueOf(alert.getNumber_of_votes()));
        int color = getColorBasedOnAlertType(alert.getAlertType().getName());
        holder.materialCardView.setStrokeColor(color);
        holder.upvote.setColorFilter(GRAY);
        holder.downvote.setColorFilter(GRAY);
        holder.upvote.setOnClickListener(v -> handleUpVote(holder.upvote, holder.downvote, holder.alertVotes, alert));
        holder.downvote.setOnClickListener(v -> handleDownVote(holder.upvote, holder.downvote, holder.alertVotes, alert));
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

    private void handleUpVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (upvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) + 1;
            if (downvote.getColorFilter().equals(RED))
                numberOfVotes++;
            upvote.setColorFilter(GREEN);
            downvote.setColorFilter(GRAY);
            alert.setNumber_of_votes(numberOfVotes);
            alertVotes.setText(String.valueOf(numberOfVotes));
        } else if (upvote.getColorFilter().equals(GREEN)) {
            upvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(--numberOfVotes));
        }
    }

    private void handleDownVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (downvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) - 1;
            if (upvote.getColorFilter().equals(GREEN))
                numberOfVotes--;
            downvote.setColorFilter(RED);
            upvote.setColorFilter(GRAY);
            alert.setNumber_of_votes(numberOfVotes);
            alertVotes.setText(String.valueOf(numberOfVotes));
        } else if (downvote.getColorFilter().equals(RED)) {
            downvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(++numberOfVotes));
        }
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView typeView;
        private final TextView rangeView;
        private final TextView alertVotes;
        public final ImageView upvote;
        private final ImageView downvote;
        private final MaterialCardView materialCardView;

        public ViewHolder(View view) {
            super(view);
            materialCardView = view.findViewById(R.id.material_card);
            titleView = view.findViewById(R.id.titleTextView);
            typeView = view.findViewById(R.id.typeTextView);
            rangeView = view.findViewById(R.id.rangeTextView);
            alertVotes = view.findViewById(R.id.alert_votes);
            upvote = view.findViewById(R.id.upvote);
            downvote = view.findViewById(R.id.downvote);
        }

    }
}