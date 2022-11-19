package edu.cs371m.visionary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import edu.cs371m.visionary.ui.HomeFragment
import edu.cs371m.visionary.ui.MainViewModel

// TODO: add back button and/or theme
class ImageActivity : AppCompatActivity() {

    companion object {
        private const val mainFragTag = "mainFragTag"
    }

    private fun addHomeFragment(definition: String) {
        // No back stack for home
        val bundle = Bundle()
        bundle.putString("definition", definition)
        val fragment = HomeFragment.newInstance()
        fragment.arguments = bundle
        supportFragmentManager.commit {
            add(R.id.main_frame, fragment, mainFragTag)
            // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            addToBackStack(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_main)
        Log.d("activity", "inside ImageActivity")
        val definition = intent.getStringExtra("definition")
        addHomeFragment(definition!!)
    }
}