package com.tutorial.earthquakemonitor.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.earthquakemonitor.Earthquake
import com.tutorial.earthquakemonitor.databinding.EqListItemBinding

private val TAG = EqAdapter::class.java.simpleName

class EqAdapter : ListAdapter<Earthquake, EqAdapter.EqViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Earthquake>() {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: (Earthquake) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqAdapter.EqViewHolder {
        val binding = EqListItemBinding.inflate(LayoutInflater.from(parent.context))
        return EqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EqAdapter.EqViewHolder, position: Int) {
        val earthquake: Earthquake = getItem(position)
        holder.bind(earthquake)
    }

    inner class EqViewHolder(private val binding: EqListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(earthquake: Earthquake) {
            binding.eqMagnitude.text = earthquake.magnitude.toString()
            binding.eqPlace.text = earthquake.place
            binding.root.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener(earthquake)
                } else {
                    Log.e(TAG, "OnItemClickListener is not initialized")
                }
            }
            //Cuando usamos binding para asignar valores a un view el binding no se ejecuta al
            // instante, sino que espera a que pase ese tiempo de 16ms para medir el área del nuevo
            // texto y luego pintarlo. Para evitarlo llamas a executePendingBindings() después de
            // que tu ViewHolder pinte los views, forzando a binding para que pinte los views inmediatamente
            binding.executePendingBindings()
        }
    }
}