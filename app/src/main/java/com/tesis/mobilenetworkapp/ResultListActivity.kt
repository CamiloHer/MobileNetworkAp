package com.tesis.mobilenetworkapp

import Adapters.ListDataAdapter
import Entities.ItemSignal
import Entities.SignalData
import Helpers.ApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesis.mobilenetworkapp.ResultActivity.Companion.EXTRA_ID
import com.tesis.mobilenetworkapp.databinding.ActivityResultListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultListActivity : AppCompatActivity() {
    private val signals = mutableListOf<ItemSignal>()

    private lateinit var listDataAdapter: ListDataAdapter

    private lateinit var binding: ActivityResultListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_list)
        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()

    }

    private fun getSignalInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseItems= getRetrofit().create(ApiService::class.java).getSignals()
            if (responseItems.isSuccessful){
                if (responseItems.body() != null) {
                    val response: List<ItemSignal>? = responseItems.body()
                    runOnUiThread {
                        if (response != null) {
                            listDataAdapter.updateList(response)
                        }
                    }
                }
            }
        }
    }

    private fun initUi() {
        listDataAdapter = ListDataAdapter(signals) { getResultSignal(it) }
        binding.rvSignalData.setHasFixedSize(true)
        binding.rvSignalData.layoutManager = LinearLayoutManager(this)
        binding.rvSignalData.adapter = listDataAdapter

        binding.btnVolver.setOnClickListener { navigateToDataColeccion() }
        getSignalInfo()
    }

    private fun getResultSignal(id: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }

    private fun navigateToDataColeccion() {
        val intent = Intent(this, DataCollectionActivity::class.java)
        startActivity(intent)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(ResultActivity.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}