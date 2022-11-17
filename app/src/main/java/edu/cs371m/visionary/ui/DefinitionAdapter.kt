package edu.cs371m.visionary.ui

import android.inputmethodservice.Keyboard.Row
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.visionary.api.DictionaryApi
import edu.cs371m.visionary.databinding.RowDefinitionBinding

class DefinitionAdapter(private val viewModel: MainViewModel, private val definitions: List<DictionaryApi.MeaningDefinition>):
    RecyclerView.Adapter<DefinitionAdapter.VH>()
{
    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(val binding: RowDefinitionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RowDefinitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        val holder = VH(binding)
        holder.binding.root.setOnClickListener {
            Log.d("adapter", "${holder.adapterPosition}")
        }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        binding.definition.text = definitions[position].definition
        // TODO: set on click to go to new activity
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

}