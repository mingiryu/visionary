package edu.cs371m.visionary

import android.text.SpannableString
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface DictionaryApi {

    @GET("api/v2/entries/en/{word>}")
    suspend fun getWordDefinitions(@Path("word") word: String)

    class DefinitionsResponse(val data: List<Definition>)

    class Definition(
        val word: String,
        val phonetic: String,
        val phonetics: List<Phonetic>,
        val origin: String,
        val meanings: List<Meaning>,
    )

    class Phonetic(
        val text: String,
        val audio: String?
    )

    class Meaning(
        val partOfSpeech: String,
        val definitions: List<MeaningDefinition>,
    )

    class MeaningDefinition(
        val definition: String,
        val example: String,
        val synonyms: List<String>,
        val antonyms: List<String>
    )

    // This class allows Retrofit to parse items in our model of type
    // SpannableString.
    class SpannableDeserializer : JsonDeserializer<SpannableString> {
        // @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): SpannableString {
            return SpannableString(json.asString)
        }
    }

    companion object {
        // Tell Gson to use our SpannableString deserializer
        private fun buildGsonConverterFactory(): GsonConverterFactory {
            val gsonBuilder = GsonBuilder().registerTypeAdapter(
                SpannableString::class.java, SpannableDeserializer()
            )
            return GsonConverterFactory.create(gsonBuilder.create())
        }

        var httpurl = HttpUrl.Builder()
            .scheme("https")
            .host("api.dictionaryapi.dev")
            .build()

        fun create(): DictionaryApi = create(httpurl)

        private fun create(httpUrl: HttpUrl): DictionaryApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    // Enable basic HTTP logging to help with debugging.
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(buildGsonConverterFactory())
                .build()
                .create(DictionaryApi::class.java)
        }
    }

}