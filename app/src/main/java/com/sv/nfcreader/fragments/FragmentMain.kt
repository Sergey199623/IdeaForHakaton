package com.sv.nfcreader.fragments

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sv.nfcreader.R
import com.sv.nfcreader.data.Account
import com.sv.nfcreader.data.repo.Repository
import java.io.File

class FragmentMain : Fragment() {

    private var nfcAdapter: NfcAdapter? = null
    private var androidBeamAvailable = false
    private val fileUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        androidBeamAvailable = when {
            activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_NFC) == true -> {
                Toast.makeText(activity, getString(R.string.enable_android_nfc), Toast.LENGTH_SHORT)
                    .show()
                false
            }
            Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                androidBeamAvailable = false
                false
            }
            else -> {
                Toast.makeText(
                    activity,
                    getString(R.string.not_has_nfc_hardware),
                    Toast.LENGTH_SHORT
                )
                    .show()
                initNfcAdapter()
                true
            }
        }

        val onAcceptData = view.findViewById<View>(R.id.btnAccept)
        val onSendData = view.findViewById<View>(R.id.btnSend)
        onSendData?.setOnClickListener {
            onSendData()
        }
        onAcceptData?.setOnClickListener {
            findNavController().navigate(R.id.fragmentDataDetails)
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
            val intent = Intent(activity, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val nfcPendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
            nfcAdapter?.enableForegroundDispatch(activity, nfcPendingIntent, null, null)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error enabling NFC foreground dispatch", ex)
        }
    }

    private fun disableNfcForegroundDispatch() {
        try {
            nfcAdapter?.disableForegroundDispatch(activity)
        } catch (ex: IllegalStateException) {
            Log.e(TAG, "Error disabling NFC foreground dispatch", ex)
        }
    }

    private fun onSendData() {

        initNfcAdapter()

        // Проверка, активен ли NFC
        when {
            nfcAdapter?.isEnabled == false -> {

                // Если NFC отключен, показываем окно настроек для включения NFC
                Toast.makeText(activity, getString(R.string.un_enable_nfc), Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            nfcAdapter?.isNdefPushEnabled == true -> {
                // Если Android Beam отключен, показываем настройки для включения Android Beam
                Toast.makeText(
                    activity, getString(R.string.need_enable_android_beam), Toast.LENGTH_SHORT
                )
                    .show()
                startActivity(Intent(Settings.ACTION_NFCSHARING_SETTINGS))
            }
            else -> {
                // Файл для отправки - пока стоит заглушка

                val gson = Gson()
                val list = listOf(
                    Account(1, "vk", "https://ibb.co/KDBVgX3", "https://vk.com/elonmusk"),
                    Account(
                        2,
                        "facebook",
                        "https://ibb.co/LN979kn",
                        "https://www.facebook.com/elonreevesmusk/"
                    ),
                    Account(3, "twitter", "https://ibb.co/sVG9qhp", "https://twitter.com/elonmusk")
                )
                val json = gson.toJson(list)
                Log.d("M_FragmentMain", json)

                val mFileUris = arrayOfNulls<Uri>(10)
//                val transferFile = "transferimage.jpg"
                val extDir: File? = requireContext().getExternalFilesDir(null)
                val requestFile = File(extDir, json)
                requestFile.setReadable(true, false)
                // Get a URI for the File and add it to the list of URIs
                // Get a URI for the File and add it to the list of URIs
                val fileUri = Uri.fromFile(requestFile)
                if (fileUri != null) {
                    mFileUris[0] = fileUri
                    Log.d("M_FragmentMain", mFileUris[0].toString())
                } else {
                    Log.e("M_FragmentMain", "No File URI available for file.")
                }


//                val fileName = "wallpaper.png"

                // Получить путь к общедоступному каталогу изображений пользователя
//                val fileDirectory = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES
//                )

                // Создаем новый файл, используя указанный каталог и имя
//                val fileToTransfer = File(fileDirectory, fileName)
//                fileToTransfer.setReadable(true, false)

                /*
                    * Instantiate a new FileUriCallback to handle requests for URIs
                    * Нужный коллбэк внизу
             */

                val fileUriCallback = FileUriCallback()
//            // Set the dynamic callback for URI requests.
                nfcAdapter?.setBeamPushUrisCallback(fileUriCallback, activity)

            }
        }
    }

    private fun initNfcAdapter() {
        val nfcManager = activity?.getSystemService(Context.NFC_SERVICE) as NfcManager
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
        val list = Repository.getAccounts()
        val json = gson.toJson(list)
        Log.d("M_FragmentMain", json)
        val sType = object : TypeToken<List<Account>>() {}.type
        val otherList = gson.fromJson<List<Account>>(json, sType)
        Log.d("M_FragmentMain", "out = $otherList")
    }

    companion object {
        const val TAG: String = "NFC"
    }
}