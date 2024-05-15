package com.tesis.mobilenetworkapp

import Entities.ItemSignal
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tesis.mobilenetworkapp.databinding.ItemSignalBinding

class DataViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSignalBinding.bind(view)
    fun render(signalData: ItemSignal, onItemSelected: (String) -> Unit) {
        binding.txtItem.text = signalData.fecha.toString()
        binding.btnVer.setOnClickListener { onItemSelected(signalData.id) }
    }
}