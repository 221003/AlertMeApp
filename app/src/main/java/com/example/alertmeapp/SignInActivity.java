package com.example.alertmeapp;

import android.content.Intent;
import android.graphics.Color;
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

import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.LoginBody;
import com.example.alertmeapp.api.RestAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;

import okhttp3.ResponseBody;
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
    private Object TextPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
        String email = emailElement.getText().toString();
        EditText passwordElement = findViewById(R.id.sign_in_password);
        String password = passwordElement.getText().toString();
        boolean emailValid = validateEmail(email);
        TextView emailInvalidElement = findViewById(R.id.emailInvalid);
        TextView passwordInvalidElement = findViewById(R.id.passwordInvalid);
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
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<ResponseBody> call = service.signIn(new LoginBody(email, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    changeActivityTo(MainActivity.class);
                else {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                displayToast();
            }
        });
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}