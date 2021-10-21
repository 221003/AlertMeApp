package com.example.alertmeapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlertMeService {
    @POST("api/users/login")
    Call<ResponseBody> login(@Body LoginBody loginBody);
}
