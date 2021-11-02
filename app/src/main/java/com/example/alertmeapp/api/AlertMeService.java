package com.example.alertmeapp.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AlertMeService {
    @POST("api/users/login")
    Call<ResponseBody> signIn(@Body LoginBody loginBody);

    @POST("api/users")
    Call<ResponseBody> signUp(@Body SignUpBody signUpBody);
}
