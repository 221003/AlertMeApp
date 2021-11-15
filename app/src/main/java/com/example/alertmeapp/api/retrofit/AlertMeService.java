package com.example.alertmeapp.api.retrofit;

import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.data.User;
import com.example.alertmeapp.api.requests.UserSignInRequest;
import com.example.alertmeapp.api.requests.UserSignUpRequest;

import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.responses.ResponseSingleData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

}
