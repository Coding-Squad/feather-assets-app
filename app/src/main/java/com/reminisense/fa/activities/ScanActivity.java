package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.Asset;
import com.reminisense.fa.models.RestResult;
import com.reminisense.fa.models.VerifyRequest;
import com.reminisense.fa.models.VerifyResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.RestClient;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USER on 5/24/2016.
 */
public class ScanActivity extends AppCompatActivity {

    /*
    Where the Verification Data Displays
     */

    @Bind(R.id.btnQr) AppCompatButton btnQr;
    @Bind(R.id.btnRfid) AppCompatButton btnRfid;
    @Bind(R.id.btnBar) AppCompatButton btnBar;
    @Bind(R.id.assetPictureData) TextView assetPictureData;
    @Bind(R.id.assetNameData) TextView assetNameData;
    @Bind(R.id.ownerNameData) TextView ownerNameData;
    @Bind(R.id.descriptionData) TextView descriptionData;
    @Bind(R.id.takeOutAvailData) TextView takeOutAvailData;
    @Bind(R.id.takeOutNoteData) TextView takeOutNoteData;

    private static final int SCAN_RFID = 1;
    private static final int SCAN_BARCODE = 2;
    private static final int SCAN_QR = 3;

    private String code = "";

    private FeatherAssetsWebService apiService;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);

        setTitle("Verify");

        //initialize api service
        apiService = new RestClient().getApiService();

        btnQr.setOnClickListener(new QrListener());
        btnRfid.setOnClickListener(new RfidListener());
        btnBar.setOnClickListener(new BarListener());
    }

    private class BarListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivityForResult(intent, SCAN_BARCODE);
        }
    }

    private class QrListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.BarcodeScannerActivity");
            startActivityForResult(intent, SCAN_QR);
        }
    }

    private class RfidListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.NfcScannerActivity");
            startActivityForResult(intent, SCAN_RFID);
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            code = data.getDataString();
            VerifyRequest verifyRequest = new VerifyRequest();
            verifyRequest.setTag(code);
            verifyRequest.setTagType(resultCode);
            verifyRequest.setCompanyId(1);

            Log.d(ScanActivity.class.toString(), verifyRequest.toString());
            verifyData(verifyRequest);
        }

    }

    private void verifyData(VerifyRequest verifyRequest){
        Call<VerifyResult> call = apiService.verify(verifyRequest);
        call.enqueue(new Callback<VerifyResult>() {
            @Override
            public void onResponse(Call<VerifyResult> call, Response<VerifyResult> response) {
                if(response.code() == 200) {
                    VerifyResult verifyResult = response.body();
                    if("OK".equals(verifyResult.getResult())) {
                        assetPictureData.setText(verifyResult.getImageUrls());
                        assetNameData.setText(verifyResult.getName());
                        descriptionData.setText(verifyResult.getDescription());
                        //ownerNameData.setText(verifyResult.getAsset().getOwnerName());
                        if(verifyResult.isTakeOutAllowed() == true ) {
                            takeOutAvailData.setText("YES");
                        } else {
                            takeOutAvailData.setText("NO");
                        }
                        takeOutNoteData.setText(verifyResult.getTakeOutInfo());
                    } else {
                        Toast.makeText(ScanActivity.this, verifyResult.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ScanActivity.this, "Error connecting to server, please try again... ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyResult> call, Throwable t) {
                Toast.makeText(ScanActivity.this, "Error connecting to server, please try again", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
