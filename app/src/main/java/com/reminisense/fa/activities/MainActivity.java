package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.reminisense.fa.managers.CacheManager;
import com.reminisense.fa.models.LoginResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginResult loginResult = CacheManager.retrieveLoginResult(MainActivity.this);
        Intent intent = new Intent();
        if (loginResult == null) {
            intent.setClassName("com.reminisense.fa",
                    "com.reminisense.fa.activities.LoginActivity");
        } else {
            Log.d(MainActivity.class.getName(), loginResult.toString());
            intent.setClassName("com.reminisense.fa",
                    "com.reminisense.fa.activities.MenuActivity");
        }
        startActivity(intent);
    }
}
