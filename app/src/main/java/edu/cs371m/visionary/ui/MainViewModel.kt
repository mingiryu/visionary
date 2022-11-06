package edu.cs371m.visionary.ui

import android.util.Log
import androidx.lifecycle.*
import edu.cs371m.visionary.api.DictionaryApi
import edu.cs371m.visionary.api.LexicaSearchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel : ViewModel() {
    private val dictionaryApi = DictionaryApi.create()
    private val lexicaSearchApi = LexicaSearchApi.create()

    private val definitions = MutableLiveData<List<DictionaryApi.Definition>?>()
    private val images = MutableLiveData<List<LexicaSearchApi.Image>>()

    private var word = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            images.postValue(lexicaSearchApi.getImages("food").images.subList(0, 10))
        }
    }

    fun observeDefinitions(): MutableLiveData<List<DictionaryApi.Definition>?> {
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

    fun netImages() {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            images.postValue(lexicaSearchApi.getImages(word.value as String).images.subList(0, 10))
        }
    }

    fun netDefinitions() {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                definitions.postValue(dictionaryApi.getWordDefinitions(word.value as String))
            } catch (e: HttpException) {
                Log.d("retrofit", "word does not exist")
                definitions.postValue(null)
            }
        }
    }
}