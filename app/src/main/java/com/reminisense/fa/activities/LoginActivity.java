package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.LoginInfo;
import com.reminisense.fa.models.LoginResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private FeatherAssetsWebService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //initialize via retrofit
        apiService = new RestClient().getApiService();

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
        LoginInfo loginInfo = new LoginInfo(email, password);

        Call<LoginResult> call = apiService.login(loginInfo);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.code() == 200) {
                    LoginResult loginResult = response.body();
                    if("OK".equals(loginResult.getResult())) {
                        CacheManager.storeLoginResult(LoginActivity.this, loginResult);
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        setMessage(MESSAGE_WELCOME);

                        Intent intent = new Intent();
                        intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.MenuActivity");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error logging in, please try again", Toast.LENGTH_LONG).show();
                        setFieldsEnabled(true);
                        setMessage(MESSAGE_FAILED);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                    setFieldsEnabled(true);
                    setMessage(MESSAGE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                setFieldsEnabled(true);
                setMessage(MESSAGE_FAILED);
            }
        });
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
