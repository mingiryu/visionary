package edu.cs371m.visionary

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LexicaSearchApiTest {
    private val lexicaSearchApi = LexicaSearchApi.create()
    private lateinit var result: LexicaSearchApi.Images

    @Before
    fun setUp() {
        runBlocking {
            result = lexicaSearchApi.getImages("apples")
        }
    }

    @Test
    fun test_prompt() {
        assertEquals("cute chubby blue fruits icons for mobile game ui ", result.images[0].prompt)
    }

    @Test
    fun test_width_height() {
        assertEquals(512, result.images[0].width)
        assertEquals(512, result.images[0].height)
    }

    @Test
    fun test_nsfw() {
        assertEquals(false, result.images[0].nsfw)
    }

    @Test
    fun sanity_check() {
        runBlocking {
            val result = lexicaSearchApi.getImages("apples")
            assertEquals("0482ee68-0368-4eca-8846-5930db866b33", result.images[0].id)
        }
    }
}
