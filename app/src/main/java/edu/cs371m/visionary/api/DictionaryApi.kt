package edu.cs371m.visionary.api

import android.text.SpannableString
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface DictionaryApi {

    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinitions(@Path("word") word: String): List<Definition>

    data class Definition(
        @SerializedName("word") val word: String,
        @SerializedName("phonetics") val phonetics: List<Phonetic>,
        @SerializedName("meanings") val meanings: List<Meaning>,
        @SerializedName("license") val license: License,
        @SerializedName("sourceUrls") val sourceUrls: List<String>
    )

    data class Phonetic(
        @SerializedName("text") val text: String?,
        @SerializedName("audio") val audio: String?,
        @SerializedName("sourceUrl") val sourceUrl: String?,
        @SerializedName("license") val license: License?,
    )

    data class Meaning(
        @SerializedName("partOfSpeech") val partOfSpeech: String,
        @SerializedName("definitions") val definitions: List<MeaningDefinition>,
        @SerializedName("synonyms") val synonyms: List<String>,
        @SerializedName("antonyms") val antonyms: List<String>
    )

    data class MeaningDefinition(
        @SerializedName("definition") val definition: String,
        @SerializedName("example") val example: String,
        @SerializedName("synonyms") val synonyms: List<String>,
        @SerializedName("antonyms") val antonyms: List<String>
    )

    data class License(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String,
    )

    // This class allows Retrofit to parse items in our model of type
    // SpannableString.
    class SpannableDeserializer : JsonDeserializer<SpannableString> {
        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement, typeOfT: Type, context: JsonDeserializationContext
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

        var httpurl = HttpUrl.Builder().scheme("https").host("api.dictionaryapi.dev").build()

        fun create(): DictionaryApi = create(httpurl)

        private fun create(httpUrl: HttpUrl): DictionaryApi {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                    // Enable basic HTTP logging to help with debugging.
                    this.level = HttpLoggingInterceptor.Level.BASIC
                }).build()
            return Retrofit.Builder().baseUrl(httpUrl).client(client)
                .addConverterFactory(buildGsonConverterFactory()).build()
                .create(DictionaryApi::class.java)
        }
    }

}