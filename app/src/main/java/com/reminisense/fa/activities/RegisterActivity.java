package com.reminisense.fa.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.User;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    // FIXME
    @Bind(R.id.txtOwner)
    AutoCompleteTextView txtOwner;
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
    AppCompatImageButton openCamera;
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
    Uri captureImageUri;


    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Feather Assets";

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

        //set autocomplete Textbox
        fetchUserName();

        btnRfidRegister.setOnClickListener(new RfidListener());
        btnQrCodeRegister.setOnClickListener(new QrListener());
        btnBarCodeRegister.setOnClickListener(new BarcodeListener());
        btnSubmit.setOnClickListener(new SubmitClickListener());
        openCamera.setOnClickListener(new OpenCameraListener());

    }

    private void fetchUserName(){
        Call<ArrayList<User>> call = apiService.getUserId(1, CacheManager.retrieveAuthToken(RegisterActivity.this));

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> user = response.body();
                ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, user);
                txtOwner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    private class OpenCameraListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureImageUri = getOutputMediaFileUri();

            intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);

            // start the image capture Intent
            startActivityForResult(intent, TAKE_PIC);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                previewCapturedImage();
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            image.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(captureImageUri.getPath(),
                    options);

            image.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
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
            asset.setImageUrls(captureImageUri.getPath());

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

    /**
     * Creating file uri to store image
     */
    public Uri getOutputMediaFileUri() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }
}