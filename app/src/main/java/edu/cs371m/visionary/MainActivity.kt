package edu.cs371m.visionary

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.visionary.databinding.ActivityMainBinding
import edu.cs371m.visionary.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ContentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        binding = activityMainBinding.contentMain

        editorAction()
        goOnClick()
    }

    private fun editorAction() {
        binding.wordET.setOnEditorActionListener { /*v*/_, actionId, event ->
            // If user has pressed enter, or if they hit the soft keyboard "send" button
            // (which sends DONE because of the XML)
            if ((event != null && (event.action == KeyEvent.ACTION_DOWN) && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideKeyboard()
                binding.goBut.callOnClick()
            }
            false
        }
    }

    private fun goOnClick() {
        binding.goBut.setOnClickListener {
            val word = binding.wordET.text.toString()

            if (word.isNotEmpty()) {
                viewModel.setWord(word)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }
}