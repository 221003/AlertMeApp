package com.example.alertmeapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
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
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.data.User;
import com.example.alertmeapp.api.requests.UserSignInRequest;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;


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
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_LOCALIZATION, REQUEST_LOCATION_CODE);
        }
        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);

                        Call<ResponseSingleData<User>> call = service.signIn(new UserSignInRequest(email, password));
                        System.out.println(new UserSignInRequest(email, password));
                        call.enqueue(new Callback<ResponseSingleData<User>>() {
                            @Override
                            public void onResponse(Call<ResponseSingleData<User>> call, Response<ResponseSingleData<User>> response) {
                                if (response.isSuccessful()) {
                                    LoggedInUser.getInstance(response.body().getData(), locationResult.getLastLocation().getLongitude(),
                                            locationResult.getLastLocation().getLatitude());
                                    changeActivityTo(MainActivity.class);
                                } else {
                                    try {
                                        System.out.println(response.code());
                                        System.out.println(response.errorBody());
                                        Gson gson = new Gson();
                                        ResponseSingleData errorResponse = gson.fromJson(
                                                response.errorBody().string(),
                                                ResponseSingleData.class);
                                        int errorCode = errorResponse.getErrorCode();
                                        String errorMessage = errorResponse.getError();
                                        if (errorCode == INCORRECT_LOGIN_CODE)
                                            emailInvalidElement.setText(errorMessage);
                                        else if (errorCode == INCORRECT_PASSWORD_CODE)
                                            passwordInvalidElement.setText(errorMessage);
                                    } catch (IOException e) {
                                        displayToast();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseSingleData<User>> call, Throwable t) {
                                displayToast();
                            }
                        });
                    }
                }, Looper.getMainLooper());
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}