package com.reminisense.fa.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

/**
 * Activity to handle registration of assets.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String Submit = "Submitting... ";
    // FIXME
//    @Bind(R.id.txtOwner)
//    EditText txtOwner;
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
    @Bind(R.id.openCamera)
    AppCompatButton openCamera;
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
    @Bind(R.id.image)
    ImageView image;

    // Request Codes
    private static final int SCAN_RFID = 1;
    private static final int SCAN_BARCODE = 2;
    private static final int SCAN_QR = 3;
    private static final int TAKE_PIC = 4;

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

        //initialize api services
        apiService = new RestClient().getApiService();

        btnRfidRegister.setOnClickListener(new RfidListener());
        btnQrCodeRegister.setOnClickListener(new QrListener());
        btnBarCodeRegister.setOnClickListener(new BarcodeListener());
        btnSubmit.setOnClickListener(new SubmitClickListener());
        openCamera.setOnClickListener(new OpenCameraListener());

    }

    private class OpenCameraListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /*File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(dir, "Demo.jpeg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            */startActivityForResult(intent, TAKE_PIC);
        }
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

    private class BarcodeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivityForResult(intent, SCAN_BARCODE);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SCAN_RFID) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_RFID);
            }
        } else if (requestCode == SCAN_BARCODE /*|| requestCode == SCAN_QR*/) {
            if (resultCode == RESULT_OK) {
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_BARCODE);
            }
        } else if (requestCode == SCAN_QR) {
            if (resultCode == RESULT_OK) {
                txtTagData.setText(data.getDataString());
                txtTagType.setText(TYPE_QRCODE);
            }
        } else if (requestCode == TAKE_PIC) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo);
            }
        }
    }

    private class SubmitClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // TODO validate
            Asset asset = new Asset();
            int companyId = CacheManager.retrieveCompanyId(RegisterActivity.this);
            // FIXME: we are assuming that there is a company with id = 1
            asset.setCompanyId(companyId == 0 ? 1 : companyId);
            // FIXME ignore owner for now
            //asset.setOwnerName(Integer.parseInt(txtOwner.getText().toString()));
            asset.setName(txtName.getText().toString());
            asset.setDescription(txtDescription.getText().toString());

            // set asset tag information
            String tagData = txtTagData.getText().toString();
            if (tagData != null) {
                asset.setTag(tagData);
            } else {
                txtTagData.setError("Please scan a tag.");
                return;
            }
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
            Call<RestResult> call = apiService.registerAsset(asset, CacheManager.retrieveAuthToken(RegisterActivity.this));
            call.enqueue(new Callback<RestResult>() {
                @Override
                public void onResponse(Call<RestResult> call, Response<RestResult> response) {
                    if (response.code() == 200) {
                        RestResult restResult = response.body();
                        Log.d(RegisterActivity.class.toString(), restResult.toString());
                        if ("OK".equals(restResult.getResult())) {
                            Toast.makeText(RegisterActivity.this, "Submit successful", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, restResult.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(RegisterActivity.class.toString(), restResult.getMessage());
                            setFieldsEnabled(true);
                        }
                    } else if (response.code() == 401) {
                        Toast.makeText(RegisterActivity.this, "Unauthorized.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error connecting to server, please try again. Error: " + response.code(), Toast.LENGTH_LONG).show();
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
                //txtOwner.setEnabled(true);
                btnSubmit.setEnabled(true);
                txtDescription.setEnabled(true);
                txtName.setEnabled(true);
                txtTakeOutInfo.setEnabled(true);
                btnBarCodeRegister.setEnabled(true);
                btnQrCodeRegister.setEnabled(true);
                btnRfidRegister.setEnabled(true);
            } else {
                //txtOwner.setEnabled(false);
                btnSubmit.setEnabled(false);
                txtDescription.setEnabled(false);
                txtName.setEnabled(false);
                txtTakeOutInfo.setEnabled(false);
                btnBarCodeRegister.setEnabled(false);
                btnQrCodeRegister.setEnabled(false);
                btnRfidRegister.setEnabled(false);
            }
        }

    }
}