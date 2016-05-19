package com.reminisense.fa.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Activity to proactively listen to an NFC tag.
 *
 * @author Nigs
 */
public abstract class NfcActivity extends AppCompatActivity {
    // NFC handling stuff
    PendingIntent pendingIntent;
    NfcAdapter nfcAdapter;

    @Override
    public void onResume() {
        super.onResume();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported on this device!", Toast.LENGTH_LONG).show();
            finish();
        } else if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC not enabled!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * Practically does nothing. Should be implemented by activity using this class.
     *
     * @param intent
     */
    public abstract void onNfcRead(Intent intent);

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            onNfcRead(intent);
        }
    }
}
