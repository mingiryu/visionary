package edu.cs371m.visionary

import android.text.SpannableString
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type

interface LexicaSearchApi {

    @GET("api/v1/search")
    suspend fun getImages(@Query("q") q: String): Images

    data class Images(
        @SerializedName("images") val images: List<Image>
    )

    data class Image(
        @SerializedName("id") val id: String,
        @SerializedName("gallery") val gallery: String,
        @SerializedName("src") val src: String,
        @SerializedName("srcSmall") val srcSmall: String,
        @SerializedName("prompt") val prompt: String,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("seed") val seed: String,
        @SerializedName("grid") val grid: Boolean,
        @SerializedName("guidance") val guidance: Int,
        @SerializedName("promptid") val promptid: String,
        @SerializedName("nsfw") val nsfw: Boolean,
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

        var httpurl = HttpUrl.Builder().scheme("https").host("lexica.art").build()

        fun create(): LexicaSearchApi = create(httpurl)

        private fun create(httpUrl: HttpUrl): LexicaSearchApi {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                // Enable basic HTTP logging to help with debugging.
                this.level = HttpLoggingInterceptor.Level.BASIC
            }).build()
            return Retrofit.Builder().baseUrl(httpUrl).client(client)
                .addConverterFactory(buildGsonConverterFactory()).build()
                .create(LexicaSearchApi::class.java)
        }
    }
}