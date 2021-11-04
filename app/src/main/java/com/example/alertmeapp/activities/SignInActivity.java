package com.example.alertmeapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.serverRequest.LoginBody;
import com.example.alertmeapp.api.RestAdapter;
import com.example.alertmeapp.api.serverResponse.ServeLogInResponse;
import com.example.alertmeapp.logedInUser.LoggedInUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private final String INVALID_EMAIL = "Email invalid";
    //TODO: provide useful error message for invalid password
    private final String EMPTY_PASSWORD = "Empty password field";
    private final String EMPTY_EMAIL = "Empty email field";
    private final String SIGN_UP_INFO = "Or sign up here";
    private final int INT_START = 11;
    private final int INT_END = 15;
    private final int INCORRECT_PASSWORD_CODE = 10;
    private final int INCORRECT_LOGIN_CODE = 11;
    private final AlertMeService service = RestAdapter.getAlertMeService();
    private TextView emailInvalidElement;
    private TextView passwordInvalidElement;
    private Object TextPaint;


    private static String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_CODE = 103;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInvalidElement = findViewById(R.id.emailInvalid);
        passwordInvalidElement = findViewById(R.id.passwordInvalid);
        signUpInfoInit();
    }

    private void signUpInfoInit() {
        TextView singUpInfo = findViewById(R.id.sign_up_info);
        singUpInfo.setHighlightColor(Color.TRANSPARENT);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //TODO: navigation to sign up activity
            }

            @Override
            public void updateDrawState(android.text.TextPaint ds) {
                if (singUpInfo.isPressed()) {
                    ds.setColor(Color.BLACK);
                } else {
                    ds.setColor(Color.BLACK);
                }
            }
        };

        SpannableString str = new SpannableString(SIGN_UP_INFO);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(clickableSpan, INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        singUpInfo.setText(str);
        singUpInfo.setMovementMethod(LinkMovementMethod.getInstance());
        singUpInfo.setOnClickListener(v -> changeActivityTo(SignUpActivity.class));
    }

    private void changeActivityTo(Class<?> activity) {
        startActivity(new Intent(getApplicationContext(), activity));
    }

    private void displayToast() {
        Toast.makeText(SignInActivity.this, "Error occurred",
                Toast.LENGTH_LONG).show();
    }

    public void onSignInClick(View view) {
        EditText emailElement = findViewById(R.id.sign_in_email);
        EditText passwordElement = findViewById(R.id.sign_in_password);
        String email = emailElement.getText().toString();
        String password = passwordElement.getText().toString();
        boolean emailValid = validateEmail(email);
        emailInvalidElement = findViewById(R.id.emailInvalid);
        passwordInvalidElement = findViewById(R.id.passwordInvalid);
        emailInvalidElement.setText("");
        passwordInvalidElement.setText("");


        if (email.isEmpty())
            emailInvalidElement.setText(EMPTY_EMAIL);
        else if (!emailValid)
            emailInvalidElement.setText(INVALID_EMAIL);
        if (password.isEmpty())
            passwordInvalidElement.setText(EMPTY_PASSWORD);

        if (emailValid && !email.isEmpty() && !password.isEmpty())
            requestToSignInUser(email, password, emailInvalidElement, passwordInvalidElement);
    }


    private void requestToSignInUser(String email, String password,
                                     TextView emailInvalidElement, TextView passwordInvalidElement) {
        getLastLocation();
        Call<ServeLogInResponse> call = service.signIn(new LoginBody(email, password));
        call.enqueue(new Callback<ServeLogInResponse>() {
            @Override
            public void onResponse(Call<ServeLogInResponse> call, Response<ServeLogInResponse> response) {
                if (response.isSuccessful()) {
                    LoggedInUser.getInstance(response.body().getUser(), longitude, latitude);
                    changeActivityTo(MainActivity.class);
                } else {
                    try {
                        String json = response.errorBody().string();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject root = jsonParser.parse(json).getAsJsonObject();
                        int errorCode = root.get("errorCode").getAsInt();
                        String errorMessage = root.get("error").getAsString();
                        if (errorCode == INCORRECT_LOGIN_CODE)
                            emailInvalidElement.setText(errorMessage);
                        else if (errorCode == INCORRECT_PASSWORD_CODE)
                            passwordInvalidElement.setText(errorMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        displayToast();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServeLogInResponse> call, Throwable t) {
                displayToast();
            }
        });

    }

    public void getLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_LOCALIZATION, REQUEST_LOCATION_CODE);
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    latitude = String.valueOf(location.getLatitude());
                                    longitude = String.valueOf(location.getLongitude());

                                } else {
                                    latitude = "52.237049";
                                    longitude = "21.017532";
                                }
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                latitude = "52.237049";
                longitude = "21.017532";
            }
        });
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}