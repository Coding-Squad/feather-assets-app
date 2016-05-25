package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.LoginResult;
import com.reminisense.fa.utils.FeatherAssetsWebService;
import com.reminisense.fa.utils.UserRolesUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nigs on 2016-05-11.
 */

public class MenuActivity extends AppCompatActivity {
    @Bind(R.id.btnRfid)
    AppCompatButton btnRfid;
    @Bind(R.id.btnQr)
    AppCompatButton btnQr;
    @Bind(R.id.btnScan)
    AppCompatButton btnScan;
    @Bind(R.id.btnRegAsset)
    AppCompatButton btnRegAsset;
    @Bind(R.id.btnRegUser)
    AppCompatButton btnRegUser;

    private FeatherAssetsWebService apiService;

    LoginResult loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        //GET USER ID
        LoginResult loginResult = CacheManager.retrieveLoginResult(MenuActivity.this);

        if (UserRolesUtil.hasRole(loginResult.getRoles(), UserRolesUtil.ROLE_ADMIN)) {
            btnRfid.setVisibility(View.VISIBLE);
            btnQr.setVisibility(View.VISIBLE);
            btnRegAsset.setVisibility(View.VISIBLE);
            btnRegUser.setVisibility(View.VISIBLE);
        } else if (UserRolesUtil.hasRole(loginResult.getRoles(), UserRolesUtil.ROLE_GUARD)) {
            btnRfid.setVisibility(View.VISIBLE);
            btnQr.setVisibility(View.VISIBLE);
            btnRegAsset.setVisibility(View.GONE);
            btnRegUser.setVisibility(View.GONE);
        } else {
            btnRfid.setVisibility(View.GONE);
            btnQr.setVisibility(View.GONE);
            btnRegAsset.setVisibility(View.GONE);
            btnRegUser.setVisibility(View.GONE);
        }

        //btnRfid.setOnClickListener(new RfidClickListener());
        //btnQr.setOnClickListener(new QrClickListener());
        btnScan.setOnClickListener(new ScanClickListener());
        btnRegAsset.setOnClickListener(new RegisterAssetClickListener());
        btnRegUser.setOnClickListener(new RegisterUserClickListener());
    }

    private class RfidClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.NfcScannerActivity");
            startActivity(intent);
        }
    }

    private class QrClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), BarcodeScannerActivity.class);
            startActivity(intent);
        }
    }

    private class ScanClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ScanActivity.class);
            startActivity(intent);
        }
    }

    private class RegisterAssetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        }
    }

    private class RegisterUserClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RegisterUserActivity.class);
            startActivity(intent);
        }
    }
}
