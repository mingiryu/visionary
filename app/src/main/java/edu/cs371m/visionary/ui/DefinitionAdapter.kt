package edu.cs371m.visionary.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.visionary.ImageActivity
import edu.cs371m.visionary.api.DictionaryApi
import edu.cs371m.visionary.databinding.RowDefinitionBinding

class DefinitionAdapter(private val viewModel: MainViewModel) :
    ListAdapter<DictionaryApi.MeaningDefinition, DefinitionAdapter.VH>(DefinitionAdapter.DefinitionDiff()) {

    class DefinitionDiff : DiffUtil.ItemCallback<DictionaryApi.MeaningDefinition>() {
        override fun areItemsTheSame(
            oldItem: DictionaryApi.MeaningDefinition, newItem: DictionaryApi.MeaningDefinition
        ): Boolean {
            return oldItem.definition == newItem.definition
        }

        override fun areContentsTheSame(
            oldItem: DictionaryApi.MeaningDefinition, newItem: DictionaryApi.MeaningDefinition
        ): Boolean {
            return (oldItem.example == newItem.example)
        }
    }

    inner class VH(val binding: RowDefinitionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowDefinitionBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val definition = getItem(holder.adapterPosition)

        binding.definition.text = definition.definition

        binding.root.setOnClickListener {
            Log.d("adapter", "starting new activity")

            val sendIntent = Intent(it.context, ImageActivity::class.java)
            sendIntent.putExtra("definition", binding.definition.text)

            viewModel.setDefinition(definition.definition)

            // Try to invoke the intent.
            try {
                it.context.startActivity(sendIntent)
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
                Log.d("ERROR", "No activity can handle intent: $e")
            }

        }
    }
}