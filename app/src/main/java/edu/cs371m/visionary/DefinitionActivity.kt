package edu.cs371m.visionary

import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.visionary.databinding.DefinitionMainBinding
import edu.cs371m.visionary.ui.DefinitionAdapter
import edu.cs371m.visionary.ui.MainViewModel

class DefinitionActivity : AppCompatActivity() {
    private lateinit var binding: DefinitionMainBinding
    private val viewModel: MainViewModel by viewModels()

    private fun initRecyclerView(binding: DefinitionMainBinding) : DefinitionAdapter {
        val definitionAdapter = DefinitionAdapter(viewModel)
        val recyclerView = binding.recyclerView

        recyclerView.adapter = definitionAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        return definitionAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val definitionMainBinding = DefinitionMainBinding.inflate(layoutInflater)
        setContentView(definitionMainBinding.root)
        binding = definitionMainBinding

        val definitionAdapter = initRecyclerView(binding)

        val word = intent.extras!!.getString("word", "")

        viewModel.setWord(word)
        viewModel.netDefinitions()

        viewModel.observeMeaningDefinitions().observe(this) {
            Log.d("definition", it.toString())
            if(it.isNullOrEmpty()) {
                // if the word has no definition
                binding.word.text = "There are no definitions for this word."
            } else {
                binding.word.text = word
                definitionAdapter.submitList(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}