package edu.cs371m.visionary

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs371m.visionary.api.DictionaryApi
import edu.cs371m.visionary.databinding.ActivityMainBinding
import edu.cs371m.visionary.databinding.ContentMainBinding
import edu.cs371m.visionary.databinding.ActionBarBinding
import edu.cs371m.visionary.ui.DefinitionAdapter
import edu.cs371m.visionary.ui.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ContentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var actionBarBinding: ActionBarBinding? = null

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    // https://stackoverflow.com/questions/24838155/set-onclick-listener-on-action-bar-title-in-android/29823008#29823008
    private fun initActionBar(actionBar: ActionBar) {
        // Disable the default and enable the custom
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBarBinding = ActionBarBinding.inflate(layoutInflater)
        // Apply the custom view
        actionBar.customView = actionBarBinding?.root
    }

    // XXX check out addTextChangedListener
    private fun actionBarSearch() {
        // XXX Write me
        actionBarBinding?.actionSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = p0.toString()
                if (text.isEmpty()) {
                    hideKeyboard()
                }
                viewModel.setWord(text)
            }
        })
    }

    private fun initRecyclerView(activityMainBinding: ActivityMainBinding, definitions: List<DictionaryApi.MeaningDefinition>) {
        // Define a layout for RecyclerView
        val layoutManager = LinearLayoutManager(this)
        activityMainBinding.contentMain.recyclerView.layoutManager = layoutManager

        // Initialize a new instance of RecyclerView Adapter instance
        val adapter = DefinitionAdapter(viewModel, definitions)
        // Set the adapter for RecyclerView
        activityMainBinding.contentMain.recyclerView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)

        binding = activityMainBinding.contentMain

        // TODO: Recycler view works, but I'm initializing it every time we click search. Can we initialize it once and update data?
        // on click listener for search button
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
                viewModel.observeDefinitions().observe(this) {
                    Log.d("dictapi", it.toString())
                    var definitions = ""
                    if(it.isNullOrEmpty()) {
                        // if the word has no definition
                        definitions = "There are no definitions for this word."
                        binding.click.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                    } else {
                        // TODO: fix the height
                        initRecyclerView(activityMainBinding, it[0].meanings[0].definitions)
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


        // clear definition
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


}