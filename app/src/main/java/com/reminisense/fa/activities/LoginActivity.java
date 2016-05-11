package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Nigs
 */
public class LoginActivity extends AppCompatActivity {

    private static final String MESSAGE_LOGGING_IN = "Logging in...";
    private static final String MESSAGE_FAILED = "Failed to log in, please try again";
    private static final String MESSAGE_WELCOME = "Welcome!";
    private static final String MESSAGE_EMPTY = "";

    @Bind(R.id.edtEmail)
    EditText email;
    @Bind(R.id.edtPassword)
    EditText password;
    @Bind(R.id.btnLogin)
    AppCompatButton login;
    @Bind(R.id.lblLoginMessage)
    TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        login.setOnClickListener(new LoginClickListener());
    }

    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String emailInput = email.getText().toString();
            String passwordInput = password.getText().toString();

            loginByEmail(emailInput, passwordInput);
            setFieldsEnabled(false);
            setMessage(MESSAGE_LOGGING_IN);
        }
    }


    private void loginByEmail(String email, String password) {

        Intent intent = new Intent();
        intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.MenuActivity");
        startActivity(intent);
        finish();
    }

    private void setFieldsEnabled(boolean enabled) {
        if (enabled) {
            email.setEnabled(true);
            password.setEnabled(true);
            login.setEnabled(true);
        } else {
            email.setEnabled(false);
            password.setEnabled(false);
            login.setEnabled(false);
        }
    }

    private void setMessage(String content) {
        message.setText(content);
    }
}
