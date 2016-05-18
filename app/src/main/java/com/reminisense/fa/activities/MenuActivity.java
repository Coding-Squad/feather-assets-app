package com.reminisense.fa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.reminisense.fa.BuildConfig;
import com.reminisense.fa.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nigs on 2016-05-11.
 */
public class MenuActivity extends AppCompatActivity {


    @Bind(R.id.btnRfid) AppCompatButton btnRfid;
    @Bind(R.id.btnQr) AppCompatButton btnQr;
    @Bind(R.id.btnReg) AppCompatButton btnReg;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        btnRfid.setOnClickListener(new RfidClickListener());
        btnQr.setOnClickListener(new QrClickListener());

    }

    private class RfidClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID + ".activities.NfcrActivity");
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
}
