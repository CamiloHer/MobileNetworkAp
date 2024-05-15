package Adapters

import Entities.ItemSignal
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesis.mobilenetworkapp.DataViewHolder
import com.tesis.mobilenetworkapp.R

class ListDataAdapter(
    private var signalData: List<ItemSignal> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<DataViewHolder>(){

    fun updateList(signalData: List<ItemSignal>){
        this.signalData = signalData
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_signal, parent, false))
    }

    override fun getItemCount(): Int =signalData.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.render(signalData[position],onItemSelected)
    }

}