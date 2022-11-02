package edu.cs371m.visionary

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.visionary.databinding.RowBinding
import edu.cs371m.visionary.glide.Glide

class RowAdapter(private val viewModel: MainViewModel) :
    ListAdapter<LexicaSearchApi.Image, RowAdapter.VH>(ImageDiff()) {

    class ImageDiff : DiffUtil.ItemCallback<LexicaSearchApi.Image>() {
        override fun areItemsTheSame(
            oldItem: LexicaSearchApi.Image,
            newItem: LexicaSearchApi.Image
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LexicaSearchApi.Image,
            newItem: LexicaSearchApi.Image
        ): Boolean {
            return (oldItem.prompt == newItem.prompt) && (oldItem.src == newItem.src) && (oldItem.srcSmall == newItem.srcSmall) && (oldItem.promptid == newItem.promptid)
        }
    }

    inner class VH(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.binding
        val image = getItem(holder.adapterPosition)

        Glide.glideFetch(image.src, image.srcSmall, binding.image)

        binding.image.setOnClickListener {
            val intent = Intent(it.context, SingleImage::class.java)

            intent.putExtra("prompt", image.prompt.toString())
            intent.putExtra("src", image.src)
            intent.putExtra("srcSmall", image.srcSmall)

            it.context.startActivity(intent)
        }
    }
}