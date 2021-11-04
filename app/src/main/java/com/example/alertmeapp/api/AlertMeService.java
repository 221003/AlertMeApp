package com.example.alertmeapp.api;

import com.example.alertmeapp.api.serverRequest.NewAlertBody;
import com.example.alertmeapp.api.serverRequest.LoginBody;
import com.example.alertmeapp.api.serverRequest.SignUpBody;
import com.example.alertmeapp.api.serverResponse.AlertTypeResponse;
import com.example.alertmeapp.api.serverResponse.AllAlertsResponse;
import com.example.alertmeapp.api.serverResponse.ServeLogInResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AlertMeService {
    @POST("api/users/login")
    Call<ServeLogInResponse> signIn(@Body LoginBody loginBody);

    @POST("api/users")
    Call<ResponseBody> signUp(@Body SignUpBody signUpBody);
    
    @GET("/api/alert-types")
    Call<AlertTypeResponse> getAllAlertTypes();

    @GET("/api/alerts")
    Call<AllAlertsResponse> getAllAlerts();

    @POST("/api/alerts")
    Call<ResponseBody> saveNewAlert(@Body NewAlertBody newAlertBody);
}
