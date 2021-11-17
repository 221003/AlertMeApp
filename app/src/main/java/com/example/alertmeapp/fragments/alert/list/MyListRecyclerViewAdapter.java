package com.example.alertmeapp.fragments.alert.list;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<AlertItem> alertList;
    private final PorterDuffColorFilter GREEN = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter RED = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    private final PorterDuffColorFilter GRAY = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private final long USER_ID = 1L;


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
        findVote(new VoteRequest(alert.getId(), USER_ID), holder);

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

    private void updateAlert(Alert alert) {
        AlertRequest alertRequest = new AlertRequest.Builder()
                .withAlertTypeId(alert.getAlertType().getId())
                .withDescription(alert.getDescription())
                .withLatitude(alert.getLatitude())
                .withLongitude(alert.getLongitude())
                .withImage(alert.getImage())
                .withTitle(alert.getTitle())
                .withNumberOfVotes(alert.getNumber_of_votes())
                .withUserId(alert.getUser().getId())
                .build();
        System.out.println(alertRequest);
        Call<ResponseSingleData<Alert>> call = service.updateAlert(alertRequest, alert.getId());
        call.enqueue(new Callback<ResponseSingleData<Alert>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Alert>> call, Response<ResponseSingleData<Alert>> response) {
                System.out.println("REQUEST OK");
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Alert>> call, Throwable t) {
            }
        });
    }

    private void handleUpVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (upvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) + 1;
            if (downvote.getColorFilter().equals(RED))
                numberOfVotes++;
            upvote.setColorFilter(GREEN);
            downvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            updateAlert(alert);
            createVote(new VoteRequest(alert.getId(), USER_ID, true));
        } else if (upvote.getColorFilter().equals(GREEN)) {
            upvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(--numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            updateAlert(alert);
            findAndDeleteVote(new VoteRequest(alert.getId(), USER_ID));
        }
    }

    private void createVote(VoteRequest voteRequest) {
        Call<ResponseSingleData<Vote>> call = service.createVote(voteRequest);
        System.out.println(voteRequest);
        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
                System.out.println("REQUEST CODE");
                System.out.println(response.code());
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {
                        ResponseSingleData errorResponse = gson.fromJson(
                                response.errorBody().string(),
                                ResponseSingleData.class);
                        int existingId = errorResponse.getErrorCode();
                        updateVote(voteRequest, (long) existingId);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });
    }

    private void updateVote(VoteRequest voteRequest, Long id) {
        Call<ResponseSingleData<Vote>> call = service.updateVote(voteRequest, id);
        System.out.println(voteRequest);
        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
                System.out.println("REQUEST CODE");
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });
    }

    private void handleDownVote(ImageView upvote, ImageView downvote, TextView alertVotes, Alert alert) {
        if (downvote.getColorFilter().equals(GRAY)) {
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString()) - 1;
            if (upvote.getColorFilter().equals(GREEN))
                numberOfVotes--;
            downvote.setColorFilter(RED);
            upvote.setColorFilter(GRAY);
            alertVotes.setText(String.valueOf(numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            updateAlert(alert);
            createVote(new VoteRequest(alert.getId(), USER_ID, false));
        } else if (downvote.getColorFilter().equals(RED)) {
            downvote.setColorFilter(GRAY);
            int numberOfVotes = Integer.parseInt(alertVotes.getText().toString());
            alertVotes.setText(String.valueOf(++numberOfVotes));
            alert.setNumber_of_votes(numberOfVotes);
            updateAlert(alert);
            findAndDeleteVote(new VoteRequest(alert.getId(), USER_ID, false));
        }
    }

    private void findVote(VoteRequest voteRequest, ViewHolder holder){
        Call<ResponseSingleData<Vote>> call = service.findVote(voteRequest);
        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
               // System.out.println("FIND VOTE CODE");
                System.out.println(response.code());
                if(response.body()!=null)
                    System.out.println("is upped" + response.body());
                if (response.body()==null){
                    holder.upvote.setColorFilter(GRAY);
                    holder.downvote.setColorFilter(GRAY);
                }
                else if(response.body().getData().isUpped())
                    holder.upvote.setColorFilter(GREEN);
                else if(!response.body().getData().isUpped())
                    holder.downvote.setColorFilter(RED);

            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });
    }

    private void deleteVote(Long voteId) {
        Call<ResponseBody> call = service.deleteVote(voteId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               // System.out.println("DELETE VOTE CODE");
                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void findAndDeleteVote(VoteRequest voteRequest) {

        Call<ResponseSingleData<Vote>> call = service.findVote(voteRequest);
        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
                //System.out.println("FIND VOTE CODE");

                deleteVote(response.body().getData().getId());
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });

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