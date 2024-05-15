package com.tesis.mobilenetworkapp

import Entities.SignalData
import Helpers.ApiService
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tesis.mobilenetworkapp.databinding.ActivityResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val BaseUrl = "http://MobileNetwork.somee.com/"
    }

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getSignalData(id)
    }

    private fun getSignalData(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseDetail = getRetrofit().create(ApiService::class.java).getSignalData(id)
            if (responseDetail.body() != null) {
                runOnUiThread { createUI(responseDetail.body()!!) }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createUI(signal: SignalData) {
        binding.txtFecha.text = "${binding.txtFecha.text} ${signal.date.toString()}"
        binding.txtLongitud.text = "${binding.txtLongitud.text} ${signal.longitude.toString()}"
        binding.txtLatitud.text = "${binding.txtLatitud.text} ${signal.latitude.toString()}"
        binding.txtLevel.text = "${binding.txtLevel.text} ${signal.level}"
        binding.txtDbm.text = "${binding.txtDbm.text} ${signal.dbm.toString()}"
        binding.txtRsrp.text = "${binding.txtRsrp.text} ${signal.rsrp.toString()}"
        binding.txtRsrq.text = "${binding.txtRsrq.text} ${signal.rsrq.toString()}"
        binding.txtRssi.text = "${binding.txtRssi.text} ${signal.rssi.toString()}"
        binding.txtOperador.text = "${binding.txtOperador.text} ${signal.operator}"
        val speedTest = when (signal.speedTest) {
            null, "" -> "N/A"
            else -> signal.speedTest
        }
        val latency = when (signal.latency){
            null->"N/A"
            else-> signal.latency.toString()
        }
        binding.txtVelociad.text = "${binding.txtVelociad.text} ${speedTest}"
        binding.txtLatencia.text = "${binding.txtLatencia.text} ${latency}"

        binding.btnResultList.setOnClickListener { navigateToResultList() }
        binding.btnVolver.setOnClickListener { navigateToDataCollection() }
    }

    private fun navigateToResultList() {
        var intent = Intent(this, ResultListActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToDataCollection() {
        var intent = Intent(this, DataCollectionActivity::class.java)
        startActivity(intent)
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}