package com.example.alertmeapp.activities;


import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.api.data.User;
import com.example.alertmeapp.api.requests.UserSignUpRequest;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.utils.FactoryAnimation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilPassword;
    private TextInputLayout tilRepeatedPassword;

    private static final String EMPTY_FIELD = "Field cannot be empty";
    private static final int USER_ALREADY_EXISTS_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView textHaveAcc = findViewById(R.id.text_have_acc);
        Button signUp = findViewById(R.id.sign_up_button);
        tilEmail = findViewById(R.id.til_email);
        tilFirstName = findViewById(R.id.til_first_name);
        tilLastName = findViewById(R.id.til_last_name);
        tilPassword = findViewById(R.id.til_password);
        tilRepeatedPassword = findViewById(R.id.til_repeated_password);
        textHaveAcc.setOnClickListener(v -> changeActivityTo(SignInActivity.class));

        signUp.setOnClickListener(v -> {
            signUp.startAnimation(FactoryAnimation.createButtonTouchedAnimation());
            if (validateFields()) {
                requestToCreateNewUser();
            }
        });
    }

    private void requestToCreateNewUser() {
        AlertMeService service = RestAdapter.getAlertMeService();
        UserSignUpRequest userSignUpRequest = createSignUpBody();
        Call<ResponseSingleData<User>> call = service.signUp(userSignUpRequest);
        call.enqueue(new Callback<ResponseSingleData<User>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<User>> call, Response<ResponseSingleData<User>> response) {
                if (response.isSuccessful()) {
                    displayToast("User successfully created");
                    changeActivityTo(MainActivity.class);
                } else {
                    Gson gson = new Gson();
                    try {
                        ResponseSingleData errorResponse = gson.fromJson(
                                response.errorBody().string(),
                                ResponseSingleData.class);
                        if (errorResponse.getErrorCode() == USER_ALREADY_EXISTS_CODE) {
                            tilEmail.setError(" ");
                            tilEmail.getEditText().setError("User already exists");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleData<User>> call, Throwable t) {
                displayToast("Error occurred");
            }
        });
    }

    private UserSignUpRequest createSignUpBody() {
        String email = tilEmail.getEditText().getText().toString();
        String firstName = tilFirstName.getEditText().getText().toString();
        String lastName = tilLastName.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        return new UserSignUpRequest(email, firstName, lastName, "", password);
    }

    private void setError(TextInputLayout til, String message) {
        til.getEditText().setError(message);
        til.setError(" ");
    }

    private boolean validateFields() {
        cleanErrors();
        boolean isValidate = true;
        String email = tilEmail.getEditText().getText().toString();
        String firstName = tilFirstName.getEditText().getText().toString();
        String lastName = tilLastName.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        String repeatedPassword = tilRepeatedPassword.getEditText().getText().toString();

        if (email.isEmpty()) {
            setError(tilEmail, EMPTY_FIELD);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(tilEmail, "Incorrect email");
        }
        if (firstName.isEmpty()) {
            setError(tilFirstName, EMPTY_FIELD);
            isValidate = false;
        }
        if (lastName.isEmpty()) {
            setError(tilLastName, EMPTY_FIELD);
            isValidate = false;
        }
        if (password.isEmpty()) {
            setError(tilPassword, EMPTY_FIELD);
            isValidate = false;
        }
        if (password.isEmpty()) {
            setError(tilPassword, EMPTY_FIELD);
            isValidate = false;
        }
        if (repeatedPassword.isEmpty()) {
            setError(tilRepeatedPassword, EMPTY_FIELD);
            isValidate = false;
        } else if (!password.equals(repeatedPassword)) {
            setError(tilRepeatedPassword, "Passwords do not match");
            isValidate = false;
        }

        return isValidate;

    }

    private void cleanErrors() {
        tilEmail.setErrorEnabled(false);
        tilFirstName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilRepeatedPassword.setErrorEnabled(false);
    }

    private void changeActivityTo(Class<?> activity) {
        Intent intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
    }

    private void displayToast(String message) {
        Toast.makeText(SignUpActivity.this, message,
                Toast.LENGTH_LONG).show();
    }
}