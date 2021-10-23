package com.example.alertmeapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText firstPasswordInput;
    private EditText secondPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView textHaveAcc = findViewById(R.id.text_have_acc);
        Button signUp = findViewById(R.id.sign_up_button);
        emailInput = findViewById(R.id.email_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        firstPasswordInput = findViewById(R.id.first_password_input);
        secondPasswordInput = findViewById(R.id.second_password_input);


        textHaveAcc.setOnClickListener(v -> changeToSignInActivity());
        signUp.setOnClickListener(v -> handleInputUserErrors());
    }


    private void handleInputUserErrors() {
        if (TextUtils.isEmpty(emailInput.getText()))
            emailInput.setError("Enter email");
        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText()).matches())
            emailInput.setError("Incorrect email");
        if (TextUtils.isEmpty(firstNameInput.getText()))
            firstNameInput.setError("Enter first name");
        if (TextUtils.isEmpty(lastNameInput.getText()))
            lastNameInput.setError("Enter last name");
        if (TextUtils.isEmpty(firstPasswordInput.getText()))
            firstPasswordInput.setError("Enter password");
        if (TextUtils.isEmpty(secondPasswordInput.getText()))
            secondPasswordInput.setError("Enter password");
        if (!secondPasswordInput.getText().toString().equals(firstNameInput.getText().toString()))
            secondPasswordInput.setError("Passwords must be the same");
    }

    private void changeToSignInActivity() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }


}