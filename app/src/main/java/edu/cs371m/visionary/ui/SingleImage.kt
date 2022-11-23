package edu.cs371m.visionary.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.cs371m.visionary.databinding.SingleImageBinding

class SingleImage  : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val singleImageBinding = SingleImageBinding.inflate(layoutInflater)
        setContentView(singleImageBinding.root)

        val prompt = intent.extras!!.getString("prompt", "")
        val srcSmall = intent.extras!!.getString("srcSmall", "")

        singleImageBinding.prompt.text = prompt
        singleImageBinding.prompt.movementMethod = ScrollingMovementMethod()
        Picasso.get()
            .load(srcSmall)
            .into(singleImageBinding.image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}