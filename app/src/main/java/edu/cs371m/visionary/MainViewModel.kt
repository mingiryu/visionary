package edu.cs371m.visionary

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val dictionaryApi = DictionaryApi.create()
    private val lexicaSearchApi = LexicaSearchApi.create()

    private val definitions = MutableLiveData<List<DictionaryApi.Definition>>()
    private val images = MutableLiveData<List<LexicaSearchApi.Image>>()

    private var word  = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            // XXX: Is this needed?
        }
    }

    fun observeDefinitions(): LiveData<List<DictionaryApi.Definition>> {
        return definitions
    }

    fun observeImages(): LiveData<List<LexicaSearchApi.Image>> {
        return images
    }

    fun observeWord(): LiveData<String> {
        return word
    }

    fun setWord(newWord: String) {
        word.value = newWord
    }
}