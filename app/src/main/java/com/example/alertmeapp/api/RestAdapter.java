package com.example.alertmeapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapter {
    private static Retrofit retrofit = null;
    private static AlertMeService alertMeService;

    public static AlertMeService getAlertMeService() {
        if(retrofit == null){
             retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            alertMeService = retrofit.create(AlertMeService.class);
        }
        return alertMeService;
    }
}
