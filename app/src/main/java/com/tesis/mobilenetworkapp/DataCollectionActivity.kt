package com.tesis.mobilenetworkapp

import Entities.ApiSaveResponse
import Entities.SignalData
import Helpers.ApiService
import Helpers.NetworkIndicatorsHelper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.tesis.mobilenetworkapp.ResultActivity.Companion.BaseUrl
import com.tesis.mobilenetworkapp.ResultActivity.Companion.EXTRA_ID
import com.tesis.mobilenetworkapp.databinding.ActivityDataCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.R)
class DataCollectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataCollectionBinding
    private val networkIndicatorsHelper: NetworkIndicatorsHelper = NetworkIndicatorsHelper()
    private lateinit var signalData: SignalData
    private lateinit var retrofit: Retrofit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collection)
        binding = ActivityDataCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()
        retrofit = getRetrofit()
        initListeners()
    }

    private fun initListeners() {
        binding.btnResult.setOnClickListener { navigateToResult() }
        binding.btnResultList.setOnClickListener { navigateToResultList() }
        binding.btnStart.setOnClickListener { checkPersissions() }
    }

    private fun navigateToResultList() {
        val intent = Intent(this, ResultListActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_ID,signalData.id)
        startActivity(intent)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getSignalData() {
        binding.txtLoader.isVisible = true
        binding.progressBar.isVisible = true
        signalData = SignalData()

        signalData = networkIndicatorsHelper.setupTelephonyManager(this@DataCollectionActivity)
        signalData.id = UUID.randomUUID().toString()
        signalData.date = LocalDateTime.now().toString()

//        lifecycleScope.launch {
//
//        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = networkIndicatorsHelper.getUserLocation(this@DataCollectionActivity)
            if (result != null){
                signalData.latitude = result.latitude
                signalData.longitude = result.longitude
            }

            val responseApi = retrofit.create(ApiService::class.java).postSaveSignal(signalData)
            if (responseApi.isSuccessful){
                val response: Boolean? = responseApi.body()
                if (response != null){
                    Log.d("bdResponse",response.toString())
                }
            }
            runOnUiThread {
                binding.txtLoader.isVisible = false
                binding.progressBar.isVisible = false
                if (result != null) {
                    binding.btnResult.isVisible = true
                    binding.txtResult.isVisible = true

                }
            }
        }

    }

    private fun checkPersissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requesPermission()
        } else {
            getSignalData()
        }
    }

    private fun requesPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_LONG)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                777
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSignalData()
            } else {
                Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_LONG)
            }
        }
    }
}