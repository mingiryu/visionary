package edu.cs371m.visionary

import android.util.Log.DEBUG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class DictionaryApiTest {
    @Test
    fun sanity_check() {
        val dictionaryApi = DictionaryApi.create()

        val scope = GlobalScope
        runBlocking {
            scope.launch {
                val result = dictionaryApi.getWordDefinitions("hello")
                assertEquals(result.data, "")
            }
        }
    }
}