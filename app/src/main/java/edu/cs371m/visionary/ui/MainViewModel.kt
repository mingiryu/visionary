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
    private val meaningDefinitions = MutableLiveData<List<DictionaryApi.MeaningDefinition>?>()
    private val images = MutableLiveData<List<LexicaSearchApi.Image>>()

    private var word = MutableLiveData<String>()
    private var definition = MutableLiveData<String>()

    fun observeDefinitions(): MutableLiveData<List<DictionaryApi.Definition>?> {
        return definitions
    }

    fun observeMeaningDefinitions(): MutableLiveData<List<DictionaryApi.MeaningDefinition>?> {
        return meaningDefinitions
    }

    fun observeImages(): LiveData<List<LexicaSearchApi.Image>> {
        return images
    }

    fun observeWord(): LiveData<String> {
        return word
    }

    fun observeDefinition(): LiveData<String> {
        return definition
    }

    fun setWord(newWord: String) {
        word.value = newWord
    }

    fun setDefinition(newDefinition: String) {
        Log.d("setting definition", newDefinition)
        definition.value = newDefinition
    }

    fun netImages(definition: String) {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                images.postValue(lexicaSearchApi.getImages(definition).images.subList(0, 50))
            } catch (e: Exception) {
                Log.d("retrofit", "$e")
                definitions.postValue(null)
            }
        }
    }

    fun netDefinitions() {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            try {
                val result: List<DictionaryApi.Definition> = dictionaryApi.getWordDefinitions(word.value as String)
                definitions.postValue(result)
                meaningDefinitions.postValue(result[0].meanings[0].definitions)
            } catch (e: HttpException) {
                Log.d("retrofit", "word does not exist")
                definitions.postValue(null)
            }
        }
    }
}