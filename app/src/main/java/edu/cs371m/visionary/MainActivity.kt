package edu.cs371m.visionary

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.visionary.databinding.ActivityMainBinding
import edu.cs371m.visionary.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ContentMainBinding

    // An Android nightmare
    // https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    // https://stackoverflow.com/questions/7789514/how-to-get-activitys-windowtoken-without-view
    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private fun initSearch() {
        binding.searchButton.setOnClickListener {
            hideKeyboard()

            val word = binding.plainTextInput.text.toString()

            if (word.isBlank()) {
                val message = "Please enter a word into the text box"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(it.context, DefinitionActivity::class.java)
                intent.putExtra("word", word)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        binding = activityMainBinding.contentMain

        initSearch()
    }
}