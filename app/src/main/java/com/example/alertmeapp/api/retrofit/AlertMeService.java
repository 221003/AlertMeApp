package com.example.alertmeapp.api.retrofit;

import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.Vote;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.data.User;
import com.example.alertmeapp.api.requests.UserSignInRequest;
import com.example.alertmeapp.api.requests.UserSignUpRequest;

import com.example.alertmeapp.api.requests.VoteRequest;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.responses.ResponseSingleData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlertMeService {
    @POST("api/users/login")
    Call<ResponseSingleData<User>> signIn(@Body UserSignInRequest userSignInRequest);

    @POST("api/users")
    Call<ResponseSingleData<User>> signUp(@Body UserSignUpRequest userSignUpRequest);

    @GET("/api/alert-types")
    Call<ResponseMultipleData<AlertType>> getAllAlertTypes();

    @GET("/api/alerts")
    Call<ResponseMultipleData<Alert>> getAllAlerts();

    @POST("/api/alerts")
    Call<ResponseBody> saveNewAlert(@Body AlertRequest alertRequest);

    @PUT("/api/alerts/{id}")
    Call<ResponseSingleData<Alert>> updateAlert(@Body AlertRequest alertRequest, @Path("id") Long id);

    @POST("/api/votes")
    Call<ResponseSingleData<Vote>> createVote(@Body VoteRequest voteRequest);

    @PUT("/api/votes/{id}")
    Call<ResponseSingleData<Vote>> updateVote(@Body VoteRequest voteRequest, @Path("id") Long id);

    @DELETE("/api/votes/{id}")
    Call<ResponseBody> deleteVote(@Path("id") Long id);

    @POST("/api/votes/find")
    Call<ResponseSingleData<Vote>> findVote(@Body VoteRequest voteRequest);

    @GET("/api/alerts/{id}")
    Call<ResponseSingleData<Alert>> getAlert(@Path("id") Long id);


    @GET("/api/alerts/latitude/{latitude}/longitude/{longitude}/accepted-distance/{distance}")
    Call<ResponseMultipleData<Alert>> getAlertByDistance(@Path("latitude") Double latitude, @Path("longitude") Double longitude,
                                                         @Path("distance") Double distance);

    @PUT("/api/users/{id}/alerts")
    Call<ResponseMultipleData<Alert>> getUserAlerts(@Path("id") Long id);

    @DELETE("/api/alerts/{id}")
    Call<ResponseBody> deleteAlert(@Path("id") Long id);
}
