package com.example.alertmeapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.User;
import com.example.alertmeapp.api.requests.AlertRequest;
import com.example.alertmeapp.api.requests.ChangePasswordRequest;
import com.example.alertmeapp.api.responses.ResponseSingleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.utils.FactoryAnimation;
import com.example.alertmeapp.utils.LoggedInUser;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {

    private TextInputEditText oldPasswordView;
    private TextInputEditText newPasswordView;
    private CheckBox turnOffNotificationsCheckBox;

    private final AlertMeService service = RestAdapter.getAlertMeService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonChangePassword = view.findViewById(R.id.change_password_btn);

        buttonChangePassword.setOnClickListener(v -> {
            buttonChangePassword.startAnimation(FactoryAnimation.createButtonTouchedAnimation());
            onChangePasswordClick(v);
        });

        oldPasswordView = view.findViewById(R.id.old_password);
        newPasswordView = view.findViewById(R.id.new_password);
        turnOffNotificationsCheckBox = view.findViewById(R.id.turnoff_notifications);

        boolean turnOffNotificationsStatus = LoggedInUser.getInstance(null, null, null).isTurnOffNotifications();
        setTurnOffNotificationsCheckBox(turnOffNotificationsStatus);

        turnOffNotificationsCheckBox.setOnClickListener(v -> {
            turnOffNotificationsCheckBox.startAnimation(FactoryAnimation.createButtonTouchedAnimation());
            setTurnOffNotificationsCheckBox(!LoggedInUser.getInstance(null, null, null).isTurnOffNotifications());
            LoggedInUser.getInstance(null, null, null).setTurnOffNotifications(!LoggedInUser.getInstance(null, null, null).isTurnOffNotifications());
            onSwitchNotificationClick(v);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean turnOffNotificationsStatus = LoggedInUser.getInstance(null, null, null).isTurnOffNotifications();
        setTurnOffNotificationsCheckBox(turnOffNotificationsStatus);
    }

    private void onChangePasswordClick(View view) {
        if (isFormValid()) {
            ChangePasswordRequest changePasswordRequest = createChangePasswordRequest();
            changePassword(changePasswordRequest);
        }
    }

    private void onSwitchNotificationClick(View view) {
        switchNotifications();
    }



    private void displayToast(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }

    private ChangePasswordRequest createChangePasswordRequest() {
        String oldPassword = oldPasswordView.getText().toString();
        String newPassword = newPasswordView.getText().toString();

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();

        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword(newPassword);

        return changePasswordRequest;
    }

    private void changePassword(ChangePasswordRequest changePasswordRequest) {
        Long userId = LoggedInUser.getInstance(null, null, null).getId();

        Call<ResponseSingleData<User>> responseBodyCall = service.changeUserPassword(changePasswordRequest, userId);
        responseBodyCall.enqueue(new Callback<ResponseSingleData<User>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<User>> call, Response<ResponseSingleData<User>> response) {
                if (response.isSuccessful()) {
                    displayToast("Password changed");
                    clearInputs();
                } else {
                    displayToast("Error on password change");
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleData<User>> call, Throwable t) {
                displayToast("Error on password change");
            }
        });
    }

    private void switchNotifications() {
        Long userId = LoggedInUser.getInstance(null, null, null).getId();

        Call<ResponseSingleData<User>> responseBodyCall = service.switchUserNotifications(userId);
        responseBodyCall.enqueue(new Callback<ResponseSingleData<User>>() {
            @Override
            public void onResponse(Call<ResponseSingleData<User>> call, Response<ResponseSingleData<User>> response) {
                if (response.isSuccessful()) {
                } else {
                    displayToast("Error on switch notifications");
                }
            }

            @Override
            public void onFailure(Call<ResponseSingleData<User>> call, Throwable t) {
                displayToast("Error on switch notifications");
            }
        });
    }

    private void clearInputs() {
        oldPasswordView.setText("");
        newPasswordView.setText("");
    }

    private void setTurnOffNotificationsCheckBox(boolean active) {
        turnOffNotificationsCheckBox.setChecked(active);
    }


    private boolean isFormValid() {
        boolean isValidate = true;
        String oldPassword = oldPasswordView.getText().toString();
        String newPassword = newPasswordView.getText().toString();
        boolean oldPasswordValid = validateNewPassword(newPassword);
        boolean newPasswordValid = validateOldPassword(oldPassword);

        if (!oldPasswordValid) {
            isValidate = false;
            displayToast("Old password is invalid.");
        }

        if (!newPasswordValid) {
            isValidate = false;
            displayToast("New password is invalid.");
        }

        return isValidate;
    }

    private boolean validateOldPassword(String oldPassword) {
        return !oldPassword.isEmpty();
    }

    private boolean validateNewPassword(String newPassword) {
        return !newPassword.isEmpty();
    }




}