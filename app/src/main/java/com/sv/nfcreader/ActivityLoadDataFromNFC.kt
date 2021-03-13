package com.sv.nfcreader

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class ActivityLoadDataFromNFC : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mAdapter == null) {
            //nfc not support your device.
            return
        }
        mPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(
                this,
                javaClass
            ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
    }

    var mAdapter: NfcAdapter? = null
    var mPendingIntent: PendingIntent? = null
    override fun onResume() {
        super.onResume()
        mAdapter!!.enableForegroundDispatch(this, mPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        if (mAdapter != null) {
            mAdapter!!.disableForegroundDispatch(this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        getDataFromTag(tag, intent)
    }

    private fun getDataFromTag(tag: Tag?, intent: Intent) {
        val ndef = Ndef.get(tag)
        try {
            ndef.connect()
            //            txtType.setText(ndef.getType().toString());
//            txtSize.setText(String.valueOf(ndef.getMaxSize()));
//            txtWrite.setText(ndef.isWritable() ? "True" : "False");
            val messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (messages != null) {
                val ndefMessages = arrayOfNulls<NdefMessage>(messages.size)
                for (i in messages.indices) {
                    ndefMessages[i] = messages[i] as NdefMessage
                }
                val record = ndefMessages[0]!!.records[0]
                val payload = record.payload
                val text = String(payload)
                Log.e("tag", "vahid$text")
                ndef.close()
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Cannot Read From Tag.", Toast.LENGTH_LONG).show()
        }
    }
}