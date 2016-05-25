package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.reminisense.fa.R;
import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.LoginResult;
import com.reminisense.fa.utils.UserRolesUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nigs on 2016-05-11.
 */
public class MenuActivity extends AppCompatActivity {
    @Bind(R.id.btnScan)
    AppCompatButton btnScan;
    @Bind(R.id.btnRegAsset)
    AppCompatButton btnRegAsset;
    @Bind(R.id.btnRegUser)
    AppCompatButton btnRegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        //GET USER ID
        LoginResult loginResult = CacheManager.retrieveLoginResult(MenuActivity.this);
        if (loginResult == null) {
            Toast.makeText(MenuActivity.this, "Login Result Invalid!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClassName("com.reminisense.fa",
                    "com.reminisense.fa.activities.LoginActivity");
            startActivity(intent);
            finish();
        }

        if (UserRolesUtil.hasRole(loginResult.getRoles(), UserRolesUtil.ROLE_ADMIN)) {
            btnScan.setVisibility(View.VISIBLE);
            btnRegAsset.setVisibility(View.VISIBLE);
            btnRegUser.setVisibility(View.VISIBLE);
        } else if (UserRolesUtil.hasRole(loginResult.getRoles(), UserRolesUtil.ROLE_GUARD)) {
            btnScan.setVisibility(View.VISIBLE);
            btnRegAsset.setVisibility(View.GONE);
            btnRegUser.setVisibility(View.GONE);
        } else {
            btnScan.setVisibility(View.GONE);
            btnRegAsset.setVisibility(View.GONE);
            btnRegUser.setVisibility(View.GONE);
        }

        btnScan.setOnClickListener(new ScanClickListener());
        btnRegAsset.setOnClickListener(new RegisterAssetClickListener());
        btnRegUser.setOnClickListener(new RegisterUserClickListener());
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
