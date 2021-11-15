package com.example.alertmeapp.dummy;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.serverRequest.AlertBody;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AlertItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<AlertItem> alertList;
    private final PorterDuffColorFilter GREEN = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter GRAY = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

    private ImageView upvote;
    private ImageView downvote;
    private TextView alertVotes;

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
        AlertBody alert = alertList.get(position).getAlertBody();
        String distance = alertList.get(position).getDistance();

        holder.titleView.setText(alert.getTitle());
        holder.typeView.setText(alert.getAlertType().getName());
        holder.rangeView.setText(distance);
        upvote = holder.upvote;
        downvote = holder.downvote;
        alertVotes = holder.alertVotes;
        holder.alertVotes.setText(String.valueOf(alert.getNumber_of_votes()));
        int color = getColorBasedOnAlertType(alert.getAlertType().getName());
        holder.materialCardView.setStrokeColor(color);
        holder.upvote.setColorFilter(GRAY);
        holder.downvote.setColorFilter(GRAY);
        AlertItem alertItem = alertList.get(position);
        holder.upvote.setOnClickListener(v -> handleUpVote());
        holder.downvote.setOnClickListener(v -> handleDownVote());
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

    private void sendVote(){

    }

    private void deleteVote(){

    }

    private void handleUpVote() {
        if (upvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) + 1;
            if (downvote.getColorFilter().equals(RED))
                numberOfVotes++;
            upvote.setColorFilter(GREEN);
            downvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
        } else if (upvote.getColorFilter().equals(GREEN)) {
            upvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(--numberOfVotes));
            deleteVote();
        }
    }

    private void handleDownVote() {
        if (downvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) - 1;
            if (upvote.getColorFilter().equals(GREEN))
                numberOfVotes--;
            downvote.setColorFilter(RED);
            upvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
        } else if (downvote.getColorFilter().equals(RED)) {
            downvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(++numberOfVotes));
            deleteVote();
        }
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView titleView;
        private final TextView typeView;
        private final TextView rangeView;
        private final TextView alertVotes;
        public final ImageView upvote;
        private final ImageView downvote;
        private final MaterialCardView materialCardView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
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