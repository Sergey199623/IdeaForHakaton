package com.sv.nfcreader

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import java.io.File


class MainActivity : Activity() {

    private var mParentPath: File? = null
    private var mIntent: Intent? = null

    private var nfcAdapter: NfcAdapter? = null
    private val pm: PackageManager = packageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем наличие NFC на устройстве
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(this, getString(R.string.not_has_nfc_hardware), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, getString(R.string.enable_android_nfc), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun onSendData(view: View) {

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Проверка, активен ли NFC
        when {
            nfcAdapter?.isEnabled == true -> {

                // Если NFC отключен, показываем окно настроек для включения NFC
                Toast.makeText(this, getString(R.string.un_enable_nfc), Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            } nfcAdapter?.isNdefPushEnabled == true -> {
            // Если Android Beam отключен, показываем настройки для включения Android Beam
                Toast.makeText(
                    this, getString(R.string.need_enable_android_beam), Toast.LENGTH_SHORT
                )
                    .show()
                startActivity(Intent(Settings.ACTION_NFCSHARING_SETTINGS))
            } else -> {

                // Файл для отправки - пока стоит заглушка
                val fileName = "wallpaper.png"

                // Получить путь к общедоступному каталогу изображений пользователя
                val fileDirectory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )

                // Создаем новый файл, используя указанный каталог и имя
                val fileToTransfer = File(fileDirectory, fileName)
                fileToTransfer.setReadable(true, false)
                nfcAdapter?.setBeamPushUris(arrayOf(Uri.fromFile(fileToTransfer)), this)
            }
        }
    }

    fun onAcceptData(view: View) {

    }
}