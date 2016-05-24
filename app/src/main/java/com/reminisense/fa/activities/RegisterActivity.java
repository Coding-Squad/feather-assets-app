package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to handle registration of assets.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String Submit = "Submitting... ";
    @Bind(R.id.txtOwner)
    EditText txtOwner;
    @Bind(R.id.txtName)
    EditText txtName;
    @Bind(R.id.txtDescription)
    EditText txtDescription;
    @Bind(R.id.txtTakeOutInfo)
    EditText txtTakeOutInfo;
    @Bind(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @Bind(R.id.btnQrCodeRegister)
    AppCompatButton btnQrCodeRegister;
    @Bind(R.id.btnRfidRegister)
    AppCompatButton btnRfidRegister;
    @Bind(R.id.btnBarCodeRegister)
    AppCompatButton btnBarCodeRegister;
    @Bind(R.id.swchTakeOut)
    Switch swchTakeOut;
    @Bind(R.id.txtTagData)
    TextView txtTagData;
    @Bind(R.id.txtTagType)
    TextView txtTagType;

    // Request Codes
    private static final int SCAN_RFID = 1;
    private static final int SCAN_BARCODE = 2;
    private static final int SCAN_QR = 3;

    // Tag type strings
    private static final String TYPE_RFID = "RFID/NFC";
    private static final String TYPE_BARCODE = "Bar Code";
    private static final String TYPE_QRCODE = "QR Code";

    private FeatherAssetsWebService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerassets);
        ButterKnife.bind(this);

        setTitle("Register Asset");

        //initialize api services
        apiService = new RestClient().getApiService();

        btnRfidRegister.setOnClickListener(new RfidListener());
        btnQrCodeRegister.setOnClickListener(new QrListener());
        btnBarCodeRegister.setOnClickListener(new BarcodeListener());
        btnSubmit.setOnClickListener(new SubmitClickListener());

    }

    private class RfidListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.NfcScannerActivity");
            startActivityForResult(intent, SCAN_RFID);
        }
    }

    private class QrListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivityForResult(intent, SCAN_QR);
        }
    }

    private class BarcodeListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivityForResult(intent, SCAN_BARCODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SCAN_RFID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_RFID);
            }
        } else if (requestCode == SCAN_BARCODE /*|| requestCode == SCAN_QR*/) {
            if(resultCode == RESULT_OK){
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_BARCODE);
            }
        } else if (requestCode == SCAN_QR){
            if(resultCode == RESULT_OK){
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_QRCODE);
            }
        }
    }

    private class SubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Asset asset = new Asset();
            asset.setOwnerName(Integer.parseInt(txtOwner.getText().toString()));
            asset.setName(txtName.getText().toString());
            asset.setDescription(txtDescription.getText().toString());

            // set asset tag information
            asset.setTag(txtTagData.getText().toString());
            String tagType = txtTagType.getText().toString();
            if (TYPE_RFID.equals(tagType)) {
                asset.setTagType(1);
            } else if (TYPE_BARCODE.equals(tagType)) {
                asset.setTagType(2);
            } else if (TYPE_QRCODE.equals(tagType)) {
                asset.setTagType(3);
            } else {
                // unknown tag
            }

            asset.setTakeOutInfo(txtTakeOutInfo.getText().toString());
            asset.setTakeOutAllowed(swchTakeOut.isChecked());

            Log.d(RegisterActivity.class.toString(), asset.toString());
            submitData(asset);
            setFieldsEnabled(false);
        }

        private void submitData(Asset asset) {
            Call<RestResult> call = apiService.registerAsset(asset);
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
                            Toast.makeText(RegisterActivity.this, restResult.getMessage(), Toast.LENGTH_LONG).show();
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

        private void setFieldsEnabled(boolean enabled) {
            if (enabled) {
                txtOwner.setEnabled(true);
                btnSubmit.setEnabled(true);
                txtDescription.setEnabled(true);
                txtName.setEnabled(true);
                txtTakeOutInfo.setEnabled(true);
                btnBarCodeRegister.setEnabled(true);
                btnQrCodeRegister.setEnabled(true);
                btnRfidRegister.setEnabled(true);
            } else {
                txtOwner.setEnabled(false);
                btnSubmit.setEnabled(false);
                txtDescription.setEnabled(false);
                txtName.setEnabled(false);
                txtTakeOutInfo.setEnabled(false);
                btnBarCodeRegister.setEnabled(true);
                btnQrCodeRegister.setEnabled(true);
                btnRfidRegister.setEnabled(true);
            }
        }

    }
}