package edu.cs371m.visionary

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DictionaryApiTest {
    private val dictionaryApi = DictionaryApi.create()
    private lateinit var result: List<DictionaryApi.Definition>

    @Before
    fun setUp() {
        runBlocking {
            result = dictionaryApi.getWordDefinitions("hello")
        }
    }

    @Test
    fun test_word() {
        assertEquals("hello", result[0].word)
    }

    @Test
    fun test_phonetics() {
        assertEquals(
            "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3",
            result[0].phonetics[0].audio
        )
        assertEquals(
            "https://commons.wikimedia.org/w/index.php?curid=75797336",
            result[0].phonetics[0].sourceUrl
        )
    }

    @Test
    fun test_meanings() {
        assertEquals("noun", result[0].meanings[0].partOfSpeech)
        assertEquals("greeting", result[0].meanings[0].synonyms[0])
        assertEquals(
            "\"Hello!\" or an equivalent greeting.",
            result[0].meanings[0].definitions[0].definition
        )
    }

    @Test
    fun sanity_check() {
        runBlocking {
            val result = dictionaryApi.getWordDefinitions("hello")
            assertEquals("hello", result[0].word)
        }
    }
}