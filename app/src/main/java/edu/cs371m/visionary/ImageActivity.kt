package edu.cs371m.visionary

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.visionary.databinding.ImageMainBinding
import edu.cs371m.visionary.ui.ImageAdapter
import edu.cs371m.visionary.ui.MainViewModel

class ImageActivity : AppCompatActivity() {
    private lateinit var binding: ImageMainBinding
    private val viewModel: MainViewModel by viewModels()

    private fun initRecyclerView(binding: ImageMainBinding): ImageAdapter {
        val imageAdapter = ImageAdapter(viewModel)
        val recyclerView = binding.recyclerView

        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        return imageAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val definitionMainBinding = ImageMainBinding.inflate(layoutInflater)
        setContentView(definitionMainBinding.root)
        binding = definitionMainBinding

        val imageAdapter = initRecyclerView(binding)

        val definition = intent.extras!!.getString("definition", "")
        viewModel.setDefinition(definition)

        if (definition.isNullOrEmpty()) {
            Log.d("Null", "No definition provided to search for images")
        } else {
            viewModel.netImages(definition)
        }

        viewModel.observeDefinition().observe(this) {
            binding.definition.text = it
        }

        viewModel.observeImages().observe(this) {
            imageAdapter.submitList(it)
        }
    }
}