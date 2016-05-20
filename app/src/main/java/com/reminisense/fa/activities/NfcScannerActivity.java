package com.reminisense.fa.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.reminisense.fa.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * An activity performing proactive scanning of NFC tags.
 */
public class NfcScannerActivity extends NfcActivity {

    @Bind(R.id.info)
    TextView textViewInfo;
    @Bind(R.id.btnOk)
    AppCompatButton btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcscanner);
        ButterKnife.bind(this);

        setTitle("RFID Tag");

        btnOk.setOnClickListener(new ButtonAcceptListener());
    }

    @Override
    public void onNfcRead(Intent intent) {

        String action = intent.getAction();
        String tagData = "";
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Toast.makeText(this, "onResume() - ACTION_TAG_DISCOVERED", Toast.LENGTH_SHORT).show();

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) {
                textViewInfo.setText("tag == null");
            } else {
                String tagInfo = tag.toString() + "\n";

                tagInfo += "\nTag Id: \n";
                byte[] tagId = tag.getId();
                tagInfo += "length = " + tagId.length + "\n";

                for (int i = 0; i < tagId.length; i++) {
                    tagData += Integer.toHexString(tagId[i] & 0xFF);
                    tagInfo += tagData + " ";
                }
                tagInfo += "\n";

                String[] techList = tag.getTechList();
                tagInfo += "\nTech List\n";
                tagInfo += "length = " + techList.length + "\n";

                for (int i = 0; i < techList.length; i++) {
                    tagInfo += techList[i] + "\n ";
                }

                textViewInfo.setText(tagInfo);
            }
        } else {
            tagData = null;
            Toast.makeText(this, "onResume() : " + action, Toast.LENGTH_SHORT).show();
        }

        Intent data = new Intent();
        data.setData(Uri.parse(tagData));
        setResult(Activity.RESULT_OK, data);
    }

    private class ButtonAcceptListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
