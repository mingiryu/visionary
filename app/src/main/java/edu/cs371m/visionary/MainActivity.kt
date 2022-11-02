package edu.cs371m.visionary

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import edu.cs371m.visionary.databinding.ActivityMainBinding
import edu.cs371m.visionary.databinding.ContentMainBinding
import edu.cs371m.visionary.databinding.ActionBarBinding
import edu.cs371m.visionary.ui.HomeFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)
        supportActionBar?.let {
            initActionBar(it)
        }
        binding = activityMainBinding.contentMain

        addHomeFragment()
        actionBarSearch()
    }


    private fun addHomeFragment() {
        // No back stack for home
        supportFragmentManager.commit {
            add(R.id.main_frame, HomeFragment.newInstance(), "mainFragTag")
            // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }


}