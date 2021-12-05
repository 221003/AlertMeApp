package com.example.alertmeapp.api.retrofit;

import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.Vote;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.requests.VoteRequest;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertMeServiceImpl {

    protected final AlertMeService service = RestAdapter.getAlertMeService();

    public void updateAlert(Alert alert) {
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

        Call<ResponseSingleData<Alert>> call = service.updateAlert(alertRequest, alert.getId());
        call.enqueue(new Callback<ResponseSingleData<Alert>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Alert>> call, Response<ResponseSingleData<Alert>> response) {
//                System.out.println("REQUEST OK");
//                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Alert>> call, Throwable t) {
            }
        });
    }

    public void createVote(VoteRequest voteRequest) {
        Call<ResponseSingleData<Vote>> call = service.createVote(voteRequest);

        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
//                System.out.println("REQUEST CODE");
//                System.out.println(response.code());
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

    public void updateVote(VoteRequest voteRequest, Long id) {
        Call<ResponseSingleData<Vote>> call = service.updateVote(voteRequest, id);

        call.enqueue(new Callback<ResponseSingleData<Vote>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<Vote>> call, Response<ResponseSingleData<Vote>> response) {
//                System.out.println("REQUEST CODE");
//                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseSingleData<Vote>> call, Throwable t) {
            }
        });
    }

    public void deleteVote(Long voteId) {
        Call<ResponseBody> call = service.deleteVote(voteId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // System.out.println("DELETE VOTE CODE");
//                System.out.println(response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void findAndDeleteVote(VoteRequest voteRequest) {

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
}
