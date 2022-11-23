package edu.cs371m.visionary

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.cs371m.visionary.api.DictionaryApi
import edu.cs371m.visionary.databinding.ActivityMainBinding
import edu.cs371m.visionary.databinding.ContentMainBinding
import edu.cs371m.visionary.databinding.RecyclerViewBinding
import edu.cs371m.visionary.ui.DefinitionAdapter
import edu.cs371m.visionary.ui.MainViewModel
import edu.cs371m.visionary.ui.RowAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ContentMainBinding
    private val viewModel: MainViewModel by viewModels()

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private fun initRecyclerView(binding: ContentMainBinding) : DefinitionAdapter {
        val definitionAdapter = DefinitionAdapter(viewModel)
        val recyclerView = binding.recyclerView

        recyclerView.adapter = definitionAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        return definitionAdapter
    }

    private fun initSearchDefinition() {
        binding.searchButton.setOnClickListener {
            hideKeyboard()

            binding.definition.text = null
            val word = binding.plainTextInput.text.toString()

            if (word.isBlank()) {
                val message = "Please enter a word into the text box"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.setWord(word)
                viewModel.netDefinitions()
            }
        }
    }

    private fun initClearDefinition() {
        binding.clear.setOnClickListener {
            if (binding.clear.visibility == View.VISIBLE) {
                binding.definition.text = null
                binding.plainTextInput.text = null
                binding.clear.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
                binding.click.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        binding = activityMainBinding.contentMain

        val definitionAdapter = initRecyclerView(binding)

        initSearchDefinition()
        initClearDefinition()

        viewModel.observeMeaningDefinitions().observe(this) {
            var definitions = ""

            if(it.isNullOrEmpty()) {
                // if the word has no definition
                definitions = "There are no definitions for this word."
                binding.click.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
            } else {
                definitionAdapter.submitList(it)
                binding.click.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
            }

            // scrolling does not work as of now
            binding.definition.text = definitions
            // make clear and search image visible
            binding.clear.visibility = View.VISIBLE
        }
    }
}