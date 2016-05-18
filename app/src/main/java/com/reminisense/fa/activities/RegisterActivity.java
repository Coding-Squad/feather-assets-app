package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String Submit = "Submitting... ";

    @Bind(R.id.companyId)
    EditText cId;
    @Bind(R.id.ownerId)
    EditText oId;
    @Bind(R.id.name)
    EditText aName;
    @Bind(R.id.description)
    EditText desc;
    @Bind(R.id.takeoutInfo)
    EditText tOutInfo;
    @Bind(R.id.submit)
    AppCompatButton submitButton;
    @Bind(R.id.QrBcodeRegister)
    AppCompatButton qrBarCode;
    @Bind(R.id.TKOgroup)
    RadioGroup tkoGroup;

    private FeatherAssetsWebService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerassets);
        ButterKnife.bind(this);

        apiService = new RestClient().getApiService();

        qrBarCode.setOnClickListener(new QrBarcodeListener());
        submitButton.setOnClickListener(new SubmitClickListener());
    }

    private class QrBarcodeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivity(intent);
        }
    }

    private class SubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            submitData();
            setFieldsEnabled(false);
        }
    }

    private void submitData() {
        Asset asset = new Asset();
        asset.setCompanyId(Integer.parseInt(cId.getText().toString()));
        asset.setOwnerId(Integer.parseInt(oId.getText().toString()));
        asset.setName(aName.getText().toString());
        asset.setDescription(desc.getText().toString());

        // FIXME
        asset.setTag("add asset code here" + new Random());
        asset.setTagType(1);
        // set barcode
        // set
        // FIXME

        asset.setTakeOutInfo(tOutInfo.getText().toString());

        Call<RestResult> call = apiService.register(asset);
        call.enqueue(new Callback<RestResult>() {
            @Override
            public void onResponse(Call<RestResult> call, Response<RestResult> response) {
                if (response.code() == 200) {
                    RestResult restResult = response.body();
                    if ("OK".equals(restResult.getResult())) {
                        Toast.makeText(RegisterActivity.this, "Submit successful", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.MenuActivity");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error submitting, please try again", Toast.LENGTH_LONG).show();
                        setFieldsEnabled(true);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                    setFieldsEnabled(true);
                }

            }

            @Override
            public void onFailure(Call<RestResult> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                t.printStackTrace();
                setFieldsEnabled(true);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // what to do when radio button is clicked
    }


    private void setFieldsEnabled(boolean enabled) {
        if (enabled) {
            cId.setEnabled(true);
            oId.setEnabled(true);
            submitButton.setEnabled(true);
            desc.setEnabled(true);
            aName.setEnabled(true);
            tOutInfo.setEnabled(true);
        } else {
            cId.setEnabled(false);
            oId.setEnabled(false);
            submitButton.setEnabled(false);
            desc.setEnabled(false);
            aName.setEnabled(false);
            tOutInfo.setEnabled(false);
        }
    }

}