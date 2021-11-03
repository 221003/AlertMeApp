package com.example.alertmeapp.api;

import java.util.List;

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

    @POST("/api/alerts")
    Call<ResponseBody> saveNewAlert(@Body AlertBody alertBody);
}
