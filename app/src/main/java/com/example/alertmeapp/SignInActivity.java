package com.example.alertmeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
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
            }
        };

        SpannableString str = new SpannableString (SIGN_UP_INFO);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(clickableSpan, INT_START, INT_END, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        singUpInfo.setText(str);
        singUpInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onSingInClick(View view) {
        //TODO: Credentials validation
        startActivity(new Intent(this, MapsActivity.class));
    }
}