package com.sv.nfcreader

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.NfcManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sv.nfcreader.data.AccountTemp
import com.sv.nfcreader.data.repo.Repository
import java.io.File

class MainActivity : Activity() {

    private var nfcAdapter: NfcAdapter? = null
    private var androidBeamAvailable = false
    private val fileUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSendData: Button = findViewById(R.id.btnSend)
        val btnAcceptData: Button = findViewById(R.id.btnAccept)

//        val pm = this.packageManager
        // Проверяем наличие NFC на устройстве
        androidBeamAvailable = if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(this, getString(R.string.not_has_nfc_hardware), Toast.LENGTH_SHORT)
                .show()
            false
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            androidBeamAvailable = false
            false
        } else {
            Toast.makeText(this, getString(R.string.enable_android_nfc), Toast.LENGTH_SHORT)
                .show()
            initNfcAdapter()
            true
        }

        btnSendData.setOnClickListener() {
            onSendData()
        }

        btnAcceptData.setOnClickListener() {
            onAcceptData()
        }
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        disableNfcForegroundDispatch()
        super.onPause()
    }

    private fun enableNfcForegroundDispatch() {
        try {
            val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error enabling NFC foreground dispatch", ex)
        }
    }

    private fun disableNfcForegroundDispatch() {
        try {
            nfcAdapter?.disableForegroundDispatch(this)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error disabling NFC foreground dispatch", ex)
        }
    }

    private fun onSendData() {

        initNfcAdapter()

        // Проверка, активен ли NFC
        when {
            nfcAdapter?.isEnabled == true -> {

                // Если NFC отключен, показываем окно настроек для включения NFC
                Toast.makeText(this, getString(R.string.un_enable_nfc), Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            nfcAdapter?.isNdefPushEnabled == true -> {
                // Если Android Beam отключен, показываем настройки для включения Android Beam
                Toast.makeText(
                    this, getString(R.string.need_enable_android_beam), Toast.LENGTH_SHORT
                )
                    .show()
                startActivity(Intent(Settings.ACTION_NFCSHARING_SETTINGS))
            }
            else -> {
                // Файл для отправки - пока стоит заглушка

                val gson = Gson()
                val list = listOf(
                    AccountTemp(1, "https://vk.com/elonmusk"),
                    AccountTemp(2, "https://www.facebook.com/elonreevesmusk/"),
                    AccountTemp(3, "https://twitter.com/elonmusk")
                )
                val json = gson.toJson(list)


                val fileName = "wallpaper.png"

                // Получить путь к общедоступному каталогу изображений пользователя
                val fileDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )

                // Создаем новый файл, используя указанный каталог и имя
                val fileToTransfer = File(fileDirectory, fileName)
                fileToTransfer.setReadable(true, false)

                /*
                    * Instantiate a new FileUriCallback to handle requests for URIs
                    * Нужный коллбэк внизу
             */

                val fileUriCallback = FileUriCallback()
//            // Set the dynamic callback for URI requests.
                nfcAdapter?.setBeamPushUrisCallback(fileUriCallback, this@MainActivity)

            }
        }
    }

    private fun initNfcAdapter() {
        val nfcManager = getSystemService(Context.NFC_SERVICE) as NfcManager
        nfcAdapter = nfcManager.defaultAdapter
    }

    private inner class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {
        /**
         * Create content URIs as needed to share with another device
         */
        override fun createBeamUris(event: NfcEvent): Array<Uri> {
            return fileUris.toTypedArray()
        }
    }

    private fun onAcceptData() {
//        Log.d("M_MainActivity", "onAccept")
        val gson = Gson()
        val list = Repository.getAccount()
        val json = gson.toJson(list)
//        Log.d("M_MainActivity", json)
        val sType = object : TypeToken<List<AccountTemp>>() {}.type
        val otherList = gson.fromJson<List<AccountTemp>>(json, sType)
        Log.d("M_MainActivity", "out = $otherList")
    }

    companion object {
        const val TAG: String = "NFC"
    }
}