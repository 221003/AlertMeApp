package com.example.alertmeapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
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
import com.example.alertmeapp.notifications.FirebaseMessageReceiver;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private final String INVALID_EMAIL = "Email invalid";
    private final String EMPTY_PASSWORD = "Empty password field";
    private final String EMPTY_EMAIL = "Empty email field";
    private final int INT_START = 20;
    private final int INT_END = 32;
    private final int INCORRECT_PASSWORD_CODE = 10;
    private final int INCORRECT_LOGIN_CODE = 11;

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextView linkToSignUpActivity;
    private final AlertMeService service = RestAdapter.getAlertMeService();


    private static final String[] PERMISSIONS_LOCALIZATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int REQUEST_LOCATION_CODE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        linkToSignUpActivity = findViewById(R.id.text_have_acc);

        SpannableString str = new SpannableString(linkToSignUpActivity.getText().toString());
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkToSignUpActivity.setText(str);
        linkToSignUpActivity.setMovementMethod(LinkMovementMethod.getInstance());
        linkToSignUpActivity.setOnClickListener(v -> changeActivityTo(SignUpActivity.class));
    }


    private void changeActivityTo(Class<?> activity) {
        startActivity(new Intent(getApplicationContext(), activity));
    }

    private void displayToast() {
        Toast.makeText(SignInActivity.this, "Error occurred",
                Toast.LENGTH_LONG).show();
    }

    public void onSignInClick(View view) {
        tilEmail.setError("");
        tilPassword.setError("");
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        boolean isEmailValid = validateEmail(email);

        if (email.isEmpty())
            tilEmail.setError(EMPTY_EMAIL);
        else if (!isEmailValid)
            tilEmail.setError(INVALID_EMAIL);

        if (password.isEmpty())
            tilPassword.setError(EMPTY_PASSWORD);

        if (isEmailValid && !email.isEmpty() && !password.isEmpty())
            requestToSignInUser(email, password);
    }


    private void requestToSignInUser(String email, String password) {
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

                        Call<ResponseSingleData<User>> call = service.signIn(new UserSignInRequest(email, password, FirebaseMessageReceiver.getToken(getApplicationContext())));
                        System.out.println(new UserSignInRequest(email, password, FirebaseMessageReceiver.getToken(getApplicationContext())));
                        call.enqueue(new Callback<ResponseSingleData<User>>() {
                            @Override
                            public void onResponse(Call<ResponseSingleData<User>> call, Response<ResponseSingleData<User>> response) {
                                if (response.isSuccessful()) {
                                    LoggedInUser.getInstance(response.body().getData(), locationResult.getLastLocation().getLongitude(),
                                            locationResult.getLastLocation().getLatitude());
                                    changeActivityTo(MainActivity.class);
                                } else {
                                    handleErrorLogIn(response);
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

    private void handleErrorLogIn(Response<ResponseSingleData<User>> response) {
        try {
            Gson gson = new Gson();
            ResponseSingleData errorResponse = gson.fromJson(
                    response.errorBody().string(),
                    ResponseSingleData.class);
            int errorCode = errorResponse.getErrorCode();
            String errorMessage = errorResponse.getError();
            if (errorCode == INCORRECT_LOGIN_CODE)
                tilEmail.setError(errorMessage);
            else if (errorCode == INCORRECT_PASSWORD_CODE)
                tilPassword.setError(errorMessage);
        } catch (IOException e) {
            displayToast();
        }
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}