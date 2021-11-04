package com.example.alertmeapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.alertmeapp.R;
import com.example.alertmeapp.activities.MainActivity;
import com.example.alertmeapp.activities.SignInActivity;
import com.example.alertmeapp.api.AlertMeService;
import com.example.alertmeapp.api.RestAdapter;
import com.example.alertmeapp.api.SignUpBody;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilPassword;
    private TextInputLayout tilRepeatedPassword;
    private TextInputLayout tilSummaryErrors;

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
        tilSummaryErrors = findViewById(R.id.til_summary_errors);
        textHaveAcc.setOnClickListener(v -> changeActivityTo(SignInActivity.class));

        signUp.setOnClickListener(v->{
            //TODO handle returned JSON message from server -> display message
            if(handleInputErrors()){
                AlertMeService service = RestAdapter.getAlertMeService();
                SignUpBody signUpBody = createSignUpBody();
                Call<ResponseBody> call = service.signUp(signUpBody);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful())
                            changeActivityTo(MainActivity.class);
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        displayToast("Error occurred");
                    }
                });
            }
        });
    }

    private SignUpBody createSignUpBody() {
        String email = tilEmail.getEditText().getText().toString();
        String firstName = tilFirstName.getEditText().getText().toString();
        String lastName = tilLastName.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        return new SignUpBody(email, firstName, lastName, "", password);
    }


    private void cleanErrors() {
        tilEmail.setErrorEnabled(false);
        tilFirstName.setErrorEnabled(false);
        tilLastName.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilRepeatedPassword.setErrorEnabled(false);
        tilSummaryErrors.setErrorEnabled(false);
    }

    private String buildOutputMessage(List<String> errorMessages) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < errorMessages.size(); i++) {
            String mess = errorMessages.get(i).toLowerCase();
            message.append(mess);
            if (i < errorMessages.size() - 1)
                message.append(", ");
        }
        return message.toString();
    }

    private Optional<String> handleEmptyField(TextInputLayout til) {
        if (TextUtils.isEmpty(til.getEditText().getText())) {
            til.setErrorEnabled(true);
            til.setError(" ");
            return Optional.of(til.getEditText().getHint().toString());
        }
        return Optional.empty();
    }

    private Optional<String> handleInputEmail(){
        String email = tilEmail.getEditText().getText().toString();
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches() || TextUtils.isEmpty(email))
            return Optional.empty();
        tilEmail.setErrorEnabled(true);
        tilEmail.setError(" ");
        return Optional.of("incorrect email");
    }

    private Optional<String> handleInputPasswords(){
        String passwordStr = tilPassword.getEditText().getText().toString();
        String repeatedPasswordStr = tilRepeatedPassword.getEditText().getText().toString();
        if (!TextUtils.isEmpty(passwordStr) && !TextUtils.isEmpty(repeatedPasswordStr)) {
            if (!repeatedPasswordStr.equals(passwordStr)){
                tilRepeatedPassword.setErrorEnabled(true);
                tilRepeatedPassword.setError(" ");
                return Optional.of("passwords must be the same");
            }
        }
        return Optional.empty();
    }

    private void displayErrorMessage(List<String> errorMessages){
        tilSummaryErrors.setErrorEnabled(true);
        tilSummaryErrors.setError(buildOutputMessage(errorMessages));
    }


    private boolean handleInputErrors() {
        cleanErrors();
        List<String> errorMessages = new ArrayList<>();
        handleEmptyField(tilEmail).ifPresent(errorMessages::add);
        handleEmptyField(tilFirstName).ifPresent(errorMessages::add);
        handleEmptyField(tilLastName).ifPresent(errorMessages::add);
        handleEmptyField(tilPassword).ifPresent(errorMessages::add);
        handleEmptyField(tilRepeatedPassword).ifPresent(errorMessages::add);
        if (!errorMessages.isEmpty())
            errorMessages.set(0, "Enter " + errorMessages.get(0));

        handleInputEmail().ifPresent(errorMessages::add);
        handleInputPasswords().ifPresent(errorMessages::add);
        displayErrorMessage(errorMessages);

        return errorMessages.isEmpty();
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