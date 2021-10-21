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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.LoginBody;
import com.example.alertmeapp.api.RestAdapter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private final String INVALID_EMAIL = "Email invalid";
    //TODO: provide useful error message for invalid password
    private final String INVALID_PASSWORD = "Password invalid";
    private final String SIGN_UP_INFO =  "Or sign up here";
    private final int INT_START = 11;
    private final int INT_END = 15;
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
            };
        };

        SpannableString str = new SpannableString (SIGN_UP_INFO);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(clickableSpan, INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        singUpInfo.setText(str);
        singUpInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onSignInClick(View view) {
        EditText emailElement = findViewById(R.id.sign_in_email);
        String email = emailElement.getText().toString();
        EditText passwordElement = findViewById(R.id.sign_in_password);
        String password = passwordElement.getText().toString();

        boolean emailValid = validateEmail(email);
      //  boolean passwordValid = validatePassword(password);
        TextView emailInvalidElement = findViewById(R.id.emailInvalid);
        TextView passwordInvalidElement = findViewById(R.id.passwordInvalid);

        if (emailValid) {
            emailInvalidElement.setText("");
        } else {
            emailInvalidElement.setText(INVALID_EMAIL);
        }

//        if (passwordValid) {
//            passwordInvalidElement.setText("");
//        } else {
//            passwordInvalidElement.setText(INVALID_PASSWORD);
//        }

        AlertMeService service = RestAdapter.getAPIClient();
        Call<ResponseBody> call = service.login(new LoginBody(email, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    emailInvalidElement.setText("");
                    passwordInvalidElement.setText("");
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                }
                else if(response.code() == 500)
                    emailInvalidElement.setText(INVALID_EMAIL);
                else if(response.code() == 400)
                    passwordInvalidElement.setText(INVALID_PASSWORD);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //TODO: Credentials validation on server
//        if (emailValid && passwordValid) {
//            startActivity(new Intent(this, MapsActivity.class));
//        }
    }

    public boolean validateEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

//    public boolean validatePassword(String password) {
//        //TODO: Change validation based on database constraints
//        System.out.println(isOK[0]);
//        return !password.isEmpty() && isOK[0];
//    }

}