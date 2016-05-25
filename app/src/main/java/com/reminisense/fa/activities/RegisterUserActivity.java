package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.User;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Marjo on 5/23/2016.
 */
public class RegisterUserActivity extends AppCompatActivity {

    private static final String Submit = "Submitting.... ";
    @Bind(R.id.fnText)
    EditText fnText;
    @Bind(R.id.lnText)
    EditText lnText;
    @Bind(R.id.posText)
    EditText posText;
    @Bind(R.id.descText)
    EditText descText;
    @Bind(R.id.emailText)
    EditText emailText;
    @Bind(R.id.btnSbmt)
    AppCompatButton btnSbmt;

    private FeatherAssetsWebService apiService;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_registeruser);
        ButterKnife.bind(this);

        setTitle("Register User");

        apiService = new RestClient().getApiService();

        btnSbmt.setOnClickListener(new SubmitClickListener());
    }

    private class SubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            User user = new User();

            user.setFirstName(fnText.getText().toString());
            user.setLastName(lnText.getText().toString());
            user.setPosition(posText.getText().toString());
            user.setDescription(descText.getText().toString());
            user.setEmail(emailText.getText().toString());

            Log.d(RegisterUserActivity.class.toString(), user.toString());
            submitData(user);
            setFieldsEnabled(false);
        }

        private void submitData(User user) {
            Call<RestResult> call = apiService.registerUser(user);
            call.enqueue(new Callback<RestResult>() {
                @Override
                public void onResponse(Call<RestResult> call, Response<RestResult> response) {
                    if (response.code() == 200) {
                        RestResult restResult = response.body();
                        if ("OK".equals(restResult.getResult())) {
                            Toast.makeText(RegisterUserActivity.this, "Submit Successful", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent();
                            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.RegisterActivity");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterUserActivity.this, restResult.getMessage(), Toast.LENGTH_LONG).show();
                            setFieldsEnabled(true);
                        }
                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                        setFieldsEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<RestResult> call, Throwable t) {
                    Toast.makeText(RegisterUserActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                    setFieldsEnabled(true);
                }
            });
        }

    }

    public void setFieldsEnabled(boolean enabled) {
        if (enabled) {
            fnText.setEnabled(true);
            lnText.setEnabled(true);
            posText.setEnabled(true);
            descText.setEnabled(true);
            emailText.setEnabled(true);
        } else {
            fnText.setEnabled(false);
            lnText.setEnabled(false);
            posText.setEnabled(false);
            descText.setEnabled(false);
            emailText.setEnabled(false);
        }
    }

}
